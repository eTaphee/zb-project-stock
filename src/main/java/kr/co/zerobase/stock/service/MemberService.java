package kr.co.zerobase.stock.service;

import static kr.co.zerobase.stock.exception.ErrorCode.PASSWORD_UN_MATCHED;
import static kr.co.zerobase.stock.exception.ErrorCode.USERNAME_ALREADY_EXISTS;
import static kr.co.zerobase.stock.exception.ErrorCode.USERNAME_NOT_FOUND;

import kr.co.zerobase.stock.exception.StockException;
import kr.co.zerobase.stock.model.Auth;
import kr.co.zerobase.stock.persist.entity.MemberEntity;
import kr.co.zerobase.stock.persist.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username)
            .orElseThrow(() -> new StockException(USERNAME_NOT_FOUND));
    }

    @Transactional
    public MemberEntity register(Auth.SignUp member) {
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new StockException(USERNAME_ALREADY_EXISTS);
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member.toEntity());
    }

    @Transactional
    public MemberEntity authenticate(Auth.SignIn member) {

        MemberEntity user = memberRepository.findByUsername(member.getUsername())
            .orElseThrow(() -> new StockException(USERNAME_NOT_FOUND));

        if (!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new StockException(PASSWORD_UN_MATCHED);
        }

        return user;
    }
}

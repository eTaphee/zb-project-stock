package kr.co.zerobase.stock.service;

import kr.co.zerobase.stock.model.Auth;
import kr.co.zerobase.stock.model.Member;
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
            .orElseThrow(() -> new UsernameNotFoundException("could not find user -> " + username));
    }

    @Transactional
    public MemberEntity register(Auth.SignUp member) {
        if (memberRepository.existsByUsername(member.getUsername())) {
            throw new RuntimeException("이미 사용 중인 아이디 입니다");
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member.toEntity());
    }

    @Transactional
    public MemberEntity authenticate(Auth.SignIn member) {

        MemberEntity user = memberRepository.findByUsername(member.getUsername())
            .orElseThrow(() -> new RuntimeException("존재 하지 않은 ID 입니다"));

        if (!passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        return user;
    }
}

package kr.co.zerobase.stock.model;

import static lombok.AccessLevel.PROTECTED;

import java.util.List;
import kr.co.zerobase.stock.persist.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Member {

    private String username;
    private List<String> roles;

    public static Member fromEntity(MemberEntity member) {
        return Member.builder()
            .username(member.getUsername())
            .roles(member.getRoles())
            .build();
    }
}

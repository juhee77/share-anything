package laheezy.community.dto.member;

import laheezy.community.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private String loginId;
    private String nickname;
    private String email;
    private LocalDateTime joinDateTime;

    //정적 팩토리 메소드 적용
    public static MemberResponseDto getInstance(Member savedMember) {
        return MemberResponseDto.builder()
                .email(savedMember.getEmail())
                .loginId(savedMember.getLoginId())
                .nickname(savedMember.getNickname())
                .joinDateTime(savedMember.getJoinDate())
                .build();
    }
}
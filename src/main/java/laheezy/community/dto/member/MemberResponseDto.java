package laheezy.community.dto.member;

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
}
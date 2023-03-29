package laheezy.community.dto.member;

import laheezy.community.domain.file.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.UrlResource;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private String loginId;
    private String nickname;
    private String email;
    private LocalDateTime joinDateTime;
    private UrlResource profileImage;
}
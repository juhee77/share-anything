package laheezy.community.dto.member;

import laheezy.community.domain.file.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.UrlResource;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class MemberDto {
    private String loginId;
    private String nickname;
    private String email;
    private LocalDateTime joinDateTime;
    private File profileImage;
}
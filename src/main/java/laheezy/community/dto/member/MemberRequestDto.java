package laheezy.community.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "멤버 회원가입 RequestDto")
public class MemberRequestDto {
    @Schema(description = "비밀번호", defaultValue = "password")
    @NotBlank
    private String password;

    @Schema(description = "로그인아이디", defaultValue = "name")
    @NotBlank
    private String loginId;

    @Schema(description = "닉네임", defaultValue = "nickname")
    @NotNull
    private String nickname;

    @Email @NotNull //email은 null을 허용 처리 한다.
    @Schema(description = "이메일", defaultValue = "email")
    private String email;

    @Schema(description = "이미지" )
    MultipartFile profileImg;
}

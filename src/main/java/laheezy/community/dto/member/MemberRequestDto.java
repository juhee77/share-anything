package laheezy.community.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "멤버 회원가입 RequestDto")
public class MemberRequestDto {
    @Schema(description = "비밀번호", defaultValue = "password")
    @NotBlank
    private String password;

    @Schema(description = "이름", defaultValue = "name")
    @NotBlank
    private String name;

    @Schema(description = "닉네임", defaultValue = "nickname")
    @NotNull
    private String nickname;

    @Email @NotNull //email은 null을 허용 처리 한다.
    @Schema(description = "이메일", defaultValue = "email")
    private String email;
}

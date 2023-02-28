package laheezy.community.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "멤버 회원가입 RequestDto")
public class MemberRequestDto {
    @Schema(description = "비밀번호", defaultValue = "password")
    private String password;
    @Schema(description = "이름", defaultValue = "name" )
    private String name;
    @Schema(description = "닉네임", defaultValue = "nickname")
    private String nickname;
    @Schema(description = "이메일", defaultValue = "email")
    private String email;
}

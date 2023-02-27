package laheezy.community.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String password;
    private String name;
    private String nickname;
    private String email;
}

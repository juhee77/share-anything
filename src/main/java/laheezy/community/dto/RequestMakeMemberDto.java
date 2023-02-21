package laheezy.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMakeMemberDto {
    private String loginId;
    private String password;
    private String name;
    private String nickname;
    private String email;
}

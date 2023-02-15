package laheezy.community.dto;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMakeDto {
    private String loginId;
    private String password;
    private String name;
    private String nickname;
}

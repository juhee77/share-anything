package laheezy.community.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String nickname;
    private int status;
    private String accessToken;
}

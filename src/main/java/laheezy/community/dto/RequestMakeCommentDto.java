package laheezy.community.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMakeCommentDto {
    @NotEmpty
    private String nickname;
    private Long postId;
    @NotEmpty
    private String text;
    private boolean open;
}

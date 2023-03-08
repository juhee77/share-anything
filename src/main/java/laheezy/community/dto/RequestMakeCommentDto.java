package laheezy.community.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestMakeCommentDto {
    @NotNull
    private Long postId;
    @NotEmpty
    private String text;
    @ColumnDefault("false")
    private boolean open;

}

package laheezy.community.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @NotEmpty
    private String text;
    @ColumnDefault("false")
    private boolean open;

}

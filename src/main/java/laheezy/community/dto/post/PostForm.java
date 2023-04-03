package laheezy.community.dto.post;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostForm {
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    @ColumnDefault("false")
    private boolean open;
    @NotEmpty
    private String board;
}

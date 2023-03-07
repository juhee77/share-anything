package laheezy.community.form;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostForm {
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    private boolean open;
}

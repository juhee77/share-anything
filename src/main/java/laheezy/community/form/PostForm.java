package laheezy.community.form;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostForm {
    @NotEmpty
    private String writerNickname; //게시글 작성자
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
}

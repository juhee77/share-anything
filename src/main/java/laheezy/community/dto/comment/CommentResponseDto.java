package laheezy.community.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private String writerNickname; //게시글 작성자
    private long postId; //포스트 id
    private String text;
    private boolean open;
}
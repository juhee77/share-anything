package laheezy.community.dto.comment;

import laheezy.community.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private String writerNickname; //게시글 작성자
    private long postId; //포스트 id
    private String text;
    private boolean open;

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        return CommentResponseDto.builder().open(comment.isOpen())
                .text(comment.getText())
                .postId(comment.getPost().getId())
                .writerNickname(comment.getMember().getNickname()).build();
    }
}
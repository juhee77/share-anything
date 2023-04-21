package laheezy.community.dto.post;

import laheezy.community.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostModifyDto {
    private String title; //제목
    private String text; //내용
    private boolean isOpen; //공개 비공개
    private Board board;

    public PostModifyDto toPostModifyDto(PostModifyRequestForm pRequestForm, Board board) {
        return PostModifyDto.builder().title(pRequestForm.getTitle()).text(pRequestForm.getText())
                .isOpen(pRequestForm.isOpen()).board(board).build();
    }
}

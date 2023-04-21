package laheezy.community.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostModifyRequestForm {
    private String title; //제목
    private String text; //내용
    private boolean isOpen; //공개 비공개
    private LocalDateTime writeDate; //작성 날짜
    private String board;

}

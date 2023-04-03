package laheezy.community.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String writer;
    private String title; //제목
    private String text; //내용
    private boolean isOpen; //공개 비공개
    private long view; // 조회수
    private LocalDateTime writeDate; //작성 날짜
    private long heartCnt;
    private String board;

}
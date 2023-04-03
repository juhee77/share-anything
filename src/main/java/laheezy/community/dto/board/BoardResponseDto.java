package laheezy.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class BoardResponseDto {
    private Long id;
    private String name;
    private LocalDateTime dateTime;
    private LocalDateTime lastmodified;
    private int openPostCnt;
}

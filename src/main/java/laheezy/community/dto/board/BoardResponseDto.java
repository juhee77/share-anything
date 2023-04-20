package laheezy.community.dto.board;

import laheezy.community.domain.Board;
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


    public static BoardResponseDto toBoardResponseDto(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .name(board.getName())
                .dateTime(board.getDateTime())
                .lastmodified(board.getLastmodified())
                .openPostCnt(0).build();
    }

}

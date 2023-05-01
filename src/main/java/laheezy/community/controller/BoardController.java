package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.dto.board.BoardResponseDto;
import laheezy.community.service.BoardService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "BoardController", description = "카테고리와 관련된 API를 담당합니다.")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping("/board/{name}")
    @Operation(summary = "board 생성")
    public BoardResponseDto makeBoard(@PathVariable("name") String name) {
        log.info("[BoardController] board 생성");
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Board board = boardService.makeBoard(Board.builder()
                .maker(nowLogin.getId())
                .active(true)
                .name(name).build());

        return BoardResponseDto.toBoardResponseDto(board);
    }

    @DeleteMapping("/board/{name}")
    @Operation(summary = "board 제거(inacive하게 변경)")
    //-> post가 없는 경우에는 제거가능하고 post가 있는 경우에는 inactive하게 변경
    public void deleteBoard(@PathVariable("name") String name) {
        log.info("[BoardController] board 제거");

        Board nowBoard = boardService.getBoardByNameInActive(name);
        boardService.deleteBoard(nowBoard);

    }


    @GetMapping("/board")
    @Operation(summary = "모든 board 확인")
    public List<BoardResponseDto> findAllBoard() {
        log.info("[BoardController] 모든 board 조회 (active한것만) ");
        return boardService.findAllBoardWithActive();
    }

}

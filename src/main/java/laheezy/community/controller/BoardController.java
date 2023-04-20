package laheezy.community.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.dto.board.BoardResponseDto;
import laheezy.community.service.BoardService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "/board")
@RequiredArgsConstructor
@Tag(name = "BoardController", description = "카테고리와 관련된 API를 담당합니다.")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping("/make/new-board")
    public BoardResponseDto makeBoard(@RequestParam("name") String name) {
        log.info("[BoardController] board 생성");
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Board board = boardService.makeBoard(Board.builder()
                .maker(nowLogin.getId())
                .active(true)
                .name(name).build());

        return BoardResponseDto.toBoardResponseDto(board);
    }


    @GetMapping("/get/all-board")
    public List<BoardResponseDto> findAllBoard() {
        log.info("[BoardController] 모든 board 조회 (active한것만) ");
        return boardService.findAllBoardWithActive();
    }

}

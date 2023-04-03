package laheezy.community.controller;

import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.dto.board.BoardResponseDto;
import laheezy.community.dto.post.PostResponseDto;
import laheezy.community.exception.Fail;
import laheezy.community.service.BoardService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value ="/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @PostMapping("/make/new-board")
    public BoardResponseDto makeBoard(@RequestParam String name) {
        log.info("[BoardController] board 생성");
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Board board = boardService.makeBoard(Board.builder()
                .maker(nowLogin.getId())
                .active(true)
                .name(name).build());

        return BoardResponseDto.builder()
                .id(board.getId())
                .name(board.getName())
                .dateTime(board.getDateTime())
                .lastmodified(board.getLastmodified())
                .openPostCnt(0).build();
    }


//    @GetMapping("/get/all-board")
//    public List<BoardResponseDto> findAllBoard(){
//        log.info("[BoardController] 모든 board 조회 (active한것만) ");
//
//    }
//
//    @GetMapping("/get/{boardName}/all-post")
//    public List<PostResponseDto> findAllPost(@PathVariable("boardName") String boardName){
//        log.info("[BoardController] 해단 board의 post 조회 (공개 되어 있는것만) ");
//
//
//
//    }



}

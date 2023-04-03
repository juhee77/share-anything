package laheezy.community.service;

import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.board.BoardResponseDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
class BoardServiceTest {
    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("열려있는 보드만 확인하는 기능, 모든 보드중에 확인하는 기능")
    public void checkBoardIdAndActive() throws Exception {
        //given
        Board board1 = Board.builder().name("test1").active(true).build();
        Board board2 = Board.builder().name("test2").active(false).build();

        //when
        Board save1 = boardService.makeBoard(board1);
        Board save2 = boardService.makeBoard(board2);

        //then
        assertThat(boardService.getBoardWithActive("test1").getId()).isEqualTo(save1.getId());
        assertThrows(RuntimeException.class, () -> boardService.getBoardWithActive("test2"));
        assertThat(boardService.getBoardById("test2").getId()).isEqualTo(save2.getId());

    }


    @Test
    @DisplayName("저장 되는지 확인한다.")
    public void checkSave() throws Exception {
        //given
        Board board1 = Board.builder().name("test1").active(true).build();
        //when
        boardService.makeBoard(board1);

        //then
        List<Board> all = boardRepository.findAll();
        assertThat(all.get(0).getName()).isEqualTo("test1");
        assertThat(all.get(0).isActive()).isEqualTo(true);
    }

    @Test
    @DisplayName("중복 이름인 경우 에러를 발생 시킨다.")
    public void checkDuplicateName() throws Exception {
        //given
        Board board1 = Board.builder().name("test1").active(true).build();
        //when
        boardService.makeBoard(board1);
        Board board2 = Board.builder().name("test1").active(true).build();

        //then
        assertThrows(RuntimeException.class, () -> {
            boardService.makeBoard(board2);
        });
    }


    @Test
    @DisplayName("열려있는 모든 Board를 출력하는지 확인")
    public void checkAllBoardWithActive() throws Exception {
        //given
        Board save1 = boardService.makeBoard(Board.builder().name("test1").active(true).build());
        Board save2 = boardService.makeBoard(Board.builder().name("test2").active(false).build());
        Board save3 = boardService.makeBoard(Board.builder().name("test3").active(true).build());
        Board save4 = boardService.makeBoard(Board.builder().name("test4").active(false).build());

        //when
        List<BoardResponseDto> allBoardWithActive = boardService.findAllBoardWithActive();

        //then(board네임은 중복되지 않는다)
        assertThat(allBoardWithActive.size()).isEqualTo(2);
        List<String> names = allBoardWithActive.stream().map(o -> o.getName()).collect(Collectors.toList());
        assertThat(names).contains("test1", "test3");
        assertThat(names).doesNotContain("test2", "test4");

    }

    @Test
    @DisplayName("Board내에 포스트 개수가 정확하게 출력되는지 확인")
    public void checkAllBoardWithPostCnt() throws Exception {
        //given
        Board board = boardService.makeBoard(Board.builder().name("test1").active(true).build());
        Member member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        Post post1 = postService.writePost(Post.builder().member(member).title("post").isOpen(true).board(board).build());
        Post post2 = postService.writePost(Post.builder().member(member).title("post").isOpen(false).board(board).build());
        Post post3 = postService.writePost(Post.builder().member(member).title("post").isOpen(true).board(board).build());

        //when
        List<BoardResponseDto> allBoardWithActive = boardService.findAllBoardWithActive();

        //then(board네임은 중복되지 않는다)
        assertThat(allBoardWithActive.get(0).getOpenPostCnt()).isEqualTo(2);
    }

}
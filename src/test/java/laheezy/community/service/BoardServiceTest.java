package laheezy.community.service;

import laheezy.community.domain.Board;
import laheezy.community.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class BoardServiceTest {
    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;

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
}
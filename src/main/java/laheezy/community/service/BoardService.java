package laheezy.community.service;


import laheezy.community.domain.Board;
import laheezy.community.dto.board.BoardResponseDto;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.BoardRepository;
import laheezy.community.repository.BoardRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static laheezy.community.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardRepositoryImpl boardRepositoryImpl;

    public Board getBoardWithActive(String board) {
        return getBoardByNameInActive(board);
    }

    public Board getBoardByNameInActive(String board) {
        Optional<Board> byName = boardRepository.findByNameAndActiveIsTrue(board);
        if (byName.isPresent()) {
            return byName.get();
        }
        throw new CustomException(INVALID_BOARD_NAME);
    }


    public Board getBoardByName(String board) {
        return boardRepository.findByName(board).get();
    }

    @Transactional
    public Board makeBoard(Board board) {
        validateDuplicateName(board.getName());
        return boardRepository.save(board);
    }

    private void validateDuplicateName(String name) {
        if (boardRepository.findByName(name).isPresent())
            throw new CustomException(DUPLICATION_BOARD_NAME);
    }


    public List<BoardResponseDto> findAllBoardWithActive() {
        return boardRepositoryImpl.findAllBoardWithActive();
    }

    @Transactional
    public void deleteBoard(Board nowBoard) {
        if (nowBoard.getPosts().size() == 0) {//그냥 제거
            boardRepository.delete(nowBoard);
        } else if (Duration.between(nowBoard.getLastmodified(), LocalDateTime.now()).toDays() <= 7) {//7일 이내에 사용된적이 있으면 삭제 불가능
            throw new CustomException(INVALID_BOARD_COMMAND);
        } else {//inactive 처리 => 7일 이내에 사용된적도 없음
            nowBoard.setInactive();
        }
    }
}

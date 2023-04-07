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

import java.util.List;
import java.util.Optional;

import static laheezy.community.exception.ErrorCode.DUPLICATION_BOARD_NAME;
import static laheezy.community.exception.ErrorCode.INVALID_BOARD_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardRepositoryImpl boardRepositoryImpl;

    public Board getBoardWithActive(String board) {
        log.info("find board in acive : {}", board);
        return getBoardByNameInActive(board);
    }

    private Board getBoardByNameInActive(String board) {
        Optional<Board> byName = boardRepository.findByNameAndActiveIsTrue(board);
        if (byName.isPresent()) {
            return byName.get();
        }
        throw new CustomException(INVALID_BOARD_NAME);
    }


    public Board getBoardById(String board) {
        log.info("find board by id : {}", board);
        return boardRepository.findByName(board).get();
    }

    @Transactional
    public Board makeBoard(Board board) {
        log.info("save board : {}", board);
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

}

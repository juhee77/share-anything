package laheezy.community.service;


import laheezy.community.domain.Board;
import laheezy.community.dto.board.BoardResponseDto;
import laheezy.community.repository.BoardRepository;
import laheezy.community.repository.BoardRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        throw new IllegalArgumentException("없거나 삭제된 board 아이디 입니다.");
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
            throw new IllegalArgumentException("해당 이름을 가진 board는 이미 존재합니다.");
    }


    public List<BoardResponseDto> findAllBoardWithActive() {
        return boardRepositoryImpl.findAllBoardWithActive();
    }

}

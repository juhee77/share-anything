package laheezy.community.service;


import laheezy.community.domain.Board;
import laheezy.community.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public Board getBoardWithActive(String board) {
        log.info("find board in acive : {}",board);
        return getBoardByNameInActive(board);
    }

    private Board getBoardByNameInActive(String board) {
        Optional<Board> byName = boardRepository.findByNameAndActiveIsTrue(board);
        if (byName.isPresent()) {
            return byName.get();
        }
        throw new IllegalArgumentException("없거나 삭제된 포스트 아이디 입니다.");
    }


    public Board getBoardById(String board){
        log.info("find board by id : {}",board);
        return boardRepository.findByName(board).get();
    }

    @Transactional
    public Board makeBoard(Board board) {
        log.info("save board : {}",board);
        return boardRepository.save(board);
    }


}

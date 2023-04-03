package laheezy.community.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import laheezy.community.domain.Board;
import laheezy.community.dto.board.BoardResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static laheezy.community.domain.QBoard.board;
import static laheezy.community.domain.QPost.post;
import static org.hibernate.internal.util.NullnessHelper.coalesce;

@Repository
@Slf4j
public class BoardRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public BoardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Board.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<BoardResponseDto> findAllBoardWithActive() {
        List<Tuple> results = jpaQueryFactory
                .select(board, post.id.count().coalesce(0L))
                .from(board)
                .leftJoin(board.posts, post)
                .where(board.active.eq(true), post.isOpen.eq(true))
                .groupBy(board)
                .fetch();

        return results.stream().map(tuple -> {
            Board b = tuple.get(board);
            long postCnt = tuple.get(post.id.count());
            return new BoardResponseDto(b.getId(), b.getName(), b.getDateTime(), b.getLastmodified(), (int) postCnt);
        }).collect(Collectors.toList());
    }
}

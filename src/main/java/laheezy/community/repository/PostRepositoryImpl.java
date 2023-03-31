package laheezy.community.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import laheezy.community.domain.Post;
import laheezy.community.dto.post.PostResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static laheezy.community.domain.QPost.post;
import static laheezy.community.domain.QPostHeart.postHeart;

@Repository
@Slf4j
public class PostRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public PostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Post.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<PostResponseDto> findAllPostWithHeartCnt() {
        log.info("findAllPost Query");
        List<Tuple> results = jpaQueryFactory
                .select(post, postHeart.id.count())
                .from(post)
                .leftJoin(post.postHearts, postHeart)
                .groupBy(post)
                .fetch();

        return results.stream().map(tuple -> {
            Post p = tuple.get(post);
            Long heartCount = tuple.get(postHeart.id.count());
            return new PostResponseDto(p.getId(), p.getMember().getNickname(), p.getTitle(), p.getText(), p.isOpen(), p.getView(), p.getWriteDate(), heartCount);
        }).collect(Collectors.toList());
    }
}

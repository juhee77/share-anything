package laheezy.community.repository;


import laheezy.community.domain.Member;
import laheezy.community.domain.Comment;
import laheezy.community.domain.CommentHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long> {
    Optional<CommentHeart> findByMemberAndComment(Member member, Comment comment);
}

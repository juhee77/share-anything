package laheezy.community.repository;


import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.PostHeart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {
    Optional<PostHeart> findByMemberAndPost(Member member, Post post);
}

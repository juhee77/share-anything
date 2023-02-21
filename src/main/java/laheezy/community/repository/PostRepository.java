package laheezy.community.repository;

import laheezy.community.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    Optional<Post> findById(Long id);
}

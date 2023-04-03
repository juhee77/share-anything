package laheezy.community.repository;

import laheezy.community.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    Optional<Board> findByNameAndActiveIsTrue(String boardName);

    Optional<Board> findByName(String boardName);
}

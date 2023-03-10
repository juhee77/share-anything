package laheezy.community.repository;

import laheezy.community.domain.Following;
import laheezy.community.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following,Long> {

    Optional<Following> findByMemberAAndMemberB(Member memberA, Member memberB);
}

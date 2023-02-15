package laheezy.community.repository;

import laheezy.community.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByNickname(String nickname);
}

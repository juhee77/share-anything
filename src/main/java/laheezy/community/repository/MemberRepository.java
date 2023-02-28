package laheezy.community.repository;

import laheezy.community.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "authority") //lazy조회가 아닌 eager 조회로 정보를 같이 가져오게 한다.
    Optional<Member> findOneWithAuthoritiesByNickname(String nickname);


    List<Member> findAll();
}

package laheezy.community.repository;

import laheezy.community.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneWithAuthoritiesByLoginId(String loginId);


    List<Member> findAll();

    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findByEmail(String email);
}

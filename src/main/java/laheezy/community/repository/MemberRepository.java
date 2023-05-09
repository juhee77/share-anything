package laheezy.community.repository;

import laheezy.community.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneWithAuthoritiesByLoginId(String loginId);

    List<Member> findAll();

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByEmail(String email);

    @Query(value = "select * from member ", nativeQuery = true)
    List<Member> findAllMemberWithDeleted();

    @Query(value = "select * from member where member_id = ?1", nativeQuery = true)
    Optional<Member> findByIdWithDeleted(Long id);
}

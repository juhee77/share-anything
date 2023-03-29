package laheezy.community.repository;

import laheezy.community.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 포스트객체생성확인() throws Exception {
        Optional<Member> juhee = memberRepository.findOneWithAuthoritiesByLoginId("juhee");
        System.out.println(juhee);

    }
}
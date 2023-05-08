package laheezy.community.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import laheezy.community.domain.Member;
import laheezy.community.dto.member.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static laheezy.community.domain.QMember.member;

@Repository
@Slf4j
public class MemberRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public MemberDto findByLoginIdWithProfile(String loginId) {
        log.info("find loginMember with Image");
        List<Tuple> results = jpaQueryFactory.select(member, member.profileImage)
                .from(member)
                //.leftJoin(member.profileImage,file)
                .where(member.loginId.eq(loginId))
                .fetch();

        if (results.isEmpty()) {
            return null;
        }
        Member m = results.get(0).get(member);
        return new MemberDto(m.getLoginId(), m.getNickname(), m.getEmail(), m.getJoinDate(), results.get(0).get(member.profileImage));
    }
}

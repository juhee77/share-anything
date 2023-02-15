package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member join(Member member) {
        //validate
        Member savedMember = memberRepository.save(member);
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        return savedMember;
    }

    public Member findByNickname(String writerNickname) {
        Member findMember = memberRepository.findByNickname(writerNickname);
        if (findMember == null) {
            log.error("id = {}",writerNickname);
            throw new RuntimeException("없는 회원입니다.");
        }
        return findMember;
    }
}

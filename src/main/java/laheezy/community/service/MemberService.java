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
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        validateDuplicateNickname(member);
        return memberRepository.save(member);
    }

    public void validateDuplicateNickname(Member member) {
        Optional<Member> byNickname = memberRepository.findByNickname(member.getNickname());
        if (byNickname.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름 입니다.");
        }
    }

    public Member findByNickname(String writerNickname) {
        Optional<Member> findMember = memberRepository.findByNickname(writerNickname);
        if (findMember.isEmpty()) {
            log.error("id = {}", writerNickname);
            throw new RuntimeException("없는 회원입니다.");
        }
        return findMember.get();
    }
}

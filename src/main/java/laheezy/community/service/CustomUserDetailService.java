package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.exception.CustomException;
import laheezy.community.exception.ErrorCode;
import laheezy.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginId) {
        return memberRepository.findOneWithAuthoritiesByLoginId(loginId)
                .map(this::createUser)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER_PROFILE));
    }

    private User createUser(Member member) {

        //활성화 되어있지 않다면(탈퇴한 멤버라면)
        if (!member.isActivated()) {
            throw new CustomException(ErrorCode.INVALID_NOW_MEMBER);
        }
        GrantedAuthority grantedAuthorities = new SimpleGrantedAuthority(member.getAuthority().toString());

        //유저 객체가 활성화 되어있다면 만들어서 리턴해준다.
        return new User(member.getLoginId(), member.getPassword(), List.of(grantedAuthorities));
    }
}

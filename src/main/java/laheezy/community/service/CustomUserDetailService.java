package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByNickname(nickname)
                .map(member -> createUser(nickname, member))
                .orElseThrow(() -> new UsernameNotFoundException(nickname + " -> 데이터 베이스에서 찾을수 없습니다"));
    }

    private User createUser(String nickName, Member member) {

        if (!member.isActivated()) {
            throw new RuntimeException(nickName + "-> 활성화 되어 있지 않습니다");
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(member.getNickname(), member.getPassword(), Collections.singleton(grantedAuthority));
    }
}

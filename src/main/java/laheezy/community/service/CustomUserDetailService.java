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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        return memberRepository.findOneWithAuthoritiesByLoginId(loginId)
                .map(member -> createUser(loginId, member))
                .orElseThrow(() -> new UsernameNotFoundException(loginId + " -> 데이터 베이스에서 찾을수 없습니다"));
    }

    private User createUser(String nickName, Member member) {

        if (!member.isActivated()) {
            throw new RuntimeException(nickName + "-> 활성화 되어 있지 않습니다");
        }
        GrantedAuthority grantedAuthorities =new SimpleGrantedAuthority(member.getAuthority().toString());


        //GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthorities().toString());

        //유저 객체가 활성화 되어있다면 만들어서 리턴해준다.
        return new User(member.getLoginId(), member.getPassword(), List.of(grantedAuthorities));
    }
}

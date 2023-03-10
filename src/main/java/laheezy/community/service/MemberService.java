package laheezy.community.service;

import laheezy.community.domain.Authority;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.jwt.RefreshToken;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.jwt.TokenRequestDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.jwt.TokenProvider;
import laheezy.community.repository.MemberRepository;
import laheezy.community.repository.jwt.RefreshTokenRepository;
import laheezy.community.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

//    @Transactional
//    public Member join(Member member) {
//        //validate
//        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
//        validateDuplicateNickname(member);
//        return memberRepository.save(member);
//    }

    @Transactional
    public Member signup(MemberRequestDto memberRequestDto) {
        //validate
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        validateDuplicateNickname(memberRequestDto);//?????? ????????? ??????
        log.info("log: {}",memberRequestDto.getNickname());

        Authority authority = Authority.builder().authorityName("ROLE_USER").build();

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .name(memberRequestDto.getName())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .activated(true)
                .authorities(Collections.singleton(authority))
                .nickname(memberRequestDto.getNickname()).build();
        return memberRepository.save(member);
    }

    public void validateDuplicateNickname(MemberRequestDto member) {
        Optional<Member> byNickname = memberRepository.findOneWithAuthoritiesByNickname(member.getNickname());
        if (byNickname.isPresent()) {
            throw new RuntimeException("?????? ???????????? ?????? ?????????.");
        }
    }

    public Member findByNickname(String writerNickname) {
        Optional<Member> findMember = memberRepository.findByNickname(writerNickname);
        if (findMember.isEmpty()) {
            log.error("id = {}", writerNickname);
            throw new RuntimeException("?????? ???????????????.");
        }
        return findMember.get();
    }

    //?????? ????????? ????????? ??????????????? ????????????
    public Optional<Member> getMemberWithAuthorities(String nickname) {
        return memberRepository.findOneWithAuthoritiesByNickname(nickname);
    }

    //?????? ???????????? ???????????? ?????? ??????????????? ?????? ????????? ??????
    public Optional<Member> getMemberWithAuthorities(){
        return SecurityUtil.getCurrentUserNickname().flatMap(memberRepository::findOneWithAuthoritiesByNickname);
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        //UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getNickname(), loginDto.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //RefreshToken ??????
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token ??????
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token ??? ???????????? ????????????.");
        }

        // 2. Access Token ?????? Member ID ????????????
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. ??????????????? Member ID ??? ???????????? Refresh Token ??? ?????????
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));

        // 4. Refresh Token ??????????????? ??????
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
        }

        // 5. ????????? ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. ????????? ?????? ????????????
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // ?????? ??????
        return tokenDto;
    }


    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    public List<Post> getMyPost(Member member) {
        return member.getPosts();
    }

    public Member findById(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isPresent()){
            return findMember.get();
        }
        throw new RequestRejectedException("?????? ?????? ?????????");
    }
}

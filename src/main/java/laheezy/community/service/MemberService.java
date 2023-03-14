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

    @Transactional
    public Member signup(MemberRequestDto memberRequestDto) {
        //validate
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        validateDuplicateNickname(memberRequestDto);//중복 닉네임 확인
        validateDuplicateEmail(memberRequestDto);//중복 닉네임 확인
        validateDuplicateLoginId(memberRequestDto);//중복 닉네임 확인
        log.info("log: {}", memberRequestDto.getNickname());

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .loginId(memberRequestDto.getLoginId())
                .nickname(memberRequestDto.getNickname())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .activated(true)
                .authority(Authority.ROLE_USER)
                .nickname(memberRequestDto.getNickname()).build();

        return memberRepository.save(member);
    }

    private void validateDuplicateLoginId(MemberRequestDto member) {
        Optional<Member> byNickname = memberRepository.findByLoginId(member.getLoginId());
        if (byNickname.isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디 입니다.");
        }
    }

    private void validateDuplicateEmail(MemberRequestDto member) {
        Optional<Member> byNickname = memberRepository.findByEmail(member.getEmail());
        if (byNickname.isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일 입니다.");
        }
    }

    public void validateDuplicateNickname(MemberRequestDto member) {
        Optional<Member> byNickname = memberRepository.findByNickname(member.getNickname());
        if (byNickname.isPresent()) {
            throw new RuntimeException("이미 존재하는 이름 입니다.");
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

    //해당 이름의 회원의 유저정보와 권환정보
    public Optional<Member> getMemberWithAuthorities(String loginId) {
        return memberRepository.findOneWithAuthoritiesByLoginId(loginId);
    }

    //현재 시큐리티 컨텍스에 있는 유저정보와 권환 정보를 준다
    public Optional<Member> getMemberWithAuthorities() {
        return SecurityUtil.getCurrentUserLoginId().flatMap(memberRepository::findOneWithAuthoritiesByLoginId);
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        //UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getNickname(), loginDto.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        //RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
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
        if (findMember.isPresent()) {
            return findMember.get();
        }
        throw new RequestRejectedException("없는 멤버 입니다");
    }


    @Transactional
    public Member modifyNickname(Member findMember, String nickname) {
        if (findMember.getLoginId().equals(nickname)) {
            throw new RequestRejectedException("현재 닉네임과 같은 네임으로는 수정이 불가능 합니다");
        }
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new RequestRejectedException("이미 사용되고 있는 닉네임 입니다");
        }
        findMember.modifyNickname(nickname); //더티 체킹 --> 트랜젝셔널 .. 제발 확인

        log.info(findMember.getNickname() + " " + findMember.getLastModified() + " " + nickname);
        return memberRepository.findById(findMember.getId()).get();
    }

    public void setAdmin(Member admin) {
        admin.setAdmin();
        log.info("ROLE이 변경 되었다 [ROLE_ADMIN]");
    }
}

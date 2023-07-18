package laheezy.community.service;

import laheezy.community.domain.Authority;
import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.jwt.RefreshToken;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.jwt.TokenRequestDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.dto.member.MemberResponseDto;
import laheezy.community.exception.CustomException;
import laheezy.community.exception.ErrorCode;
import laheezy.community.jwt.TokenProvider;
import laheezy.community.repository.MemberRepository;
import laheezy.community.repository.MemberRepositoryImpl;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static laheezy.community.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final MemberRepositoryImpl memberRepositoryImpl;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FileService fileService;

    @Transactional
    public Member signup(MemberRequestDto memberRequestDto) {
        //validate
        //log.info("coupon:{}", coupon.getDiscount(),coupon.getDiscountType());
        validateDuplicateNickname(memberRequestDto);//중복 닉네임 확인
        validateDuplicateEmail(memberRequestDto);//중복 이메일 확인
        validateDuplicateLoginId(memberRequestDto);//중복 로그인아이디 확인
        log.info("log: 회원 가입 성공 {}", memberRequestDto.getNickname());

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .loginId(memberRequestDto.getLoginId())
                .nickname(memberRequestDto.getNickname())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .activated(true)
                .authority(Authority.ROLE_USER)
                .nickname(memberRequestDto.getNickname()).build();
        memberRepository.save(member);
        return member;
    }

    private void validateDuplicateLoginId(MemberRequestDto member) {
        Optional<Member> byLoginId = memberRepository.findByLoginId(member.getLoginId());
        if (byLoginId.isPresent()) {
            throw new CustomException(ErrorCode.ID_DUPLICATION);
        }
    }

    private void validateDuplicateEmail(MemberRequestDto member) {
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        if (byEmail.isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        }
    }

    public void validateDuplicateNickname(MemberRequestDto member) {
        Optional<Member> byNickname = memberRepository.findByNickname(member.getNickname());
        if (byNickname.isPresent()) {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATION);

        }
    }

    public Member findByNickname(String writerNickname) {
        Optional<Member> findMember = memberRepository.findByNickname(writerNickname);
        if (findMember.isEmpty()) {
            log.error("id = {}", writerNickname);
            throw new CustomException(ErrorCode.INVALID_MEMBER_NICKNAME);
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


    public MemberDto getMemberByLoginId(String loginId) {
        return memberRepositoryImpl.findByLoginIdWithProfile(loginId);
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
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
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(INVALID_TOKEN_INFO);
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new CustomException(ALREADY_LOGOUT));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(INVALID_TOKEN_WITH_USER);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());//RTR
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }


    @Transactional
    public void logout(Member findMember) {
        if (refreshTokenRepository.findByKey(findMember.getLoginId()).isPresent()) {
            refreshTokenRepository.deleteByKey(findMember.getLoginId());

        } else {
            throw new CustomException(ALREADY_LOGOUT);
        }
    }

    @Transactional
    public void deleteMember(Member findMember) {
        memberRepository.delete(findMember);
    }

    public List<Post> getMyPost(Member member) {
        return member.getPosts();
    }

    public List<Comment> getMyComment(Member member) {
        return member.getComments();
    }

    public List<MemberResponseDto> findAllMember() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::getInstance)
                .toList();
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
        if (findMember.getNickname().equals(nickname)) {
            throw new CustomException(NICKNAME_SAME_BEFORE);
        }
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new CustomException(NICKNAME_DUPLICATION);
        }
        findMember.modifyNickname(nickname);

        log.info(findMember.getNickname() + " " + findMember.getLastModified() + " " + nickname);
        return memberRepository.findById(findMember.getId()).get();
    }

    @Transactional
    public void setAdmin(Member admin) {
        admin.setAdmin();
        log.info("[ROLE_ADMIN] ROLE이 변경 되었다");
    }

    public void checkPassword(Member findMember, String exPassword) {
        if (!passwordEncoder.matches(exPassword, findMember.getPassword()))
            throw new CustomException(INVALID_BEFORE_PASSWORD);
    }

    @Transactional
    public void modifyPassword(Member findMember, String newPassword) {
        //token 삭제가 불가능 Redis로 변경하거나 JWT를 그냥 타임아웃하도록한다.
        findMember.modifyPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void dropProfileImage(Member member) {
        //이미지 드랍
        member.setProfile(null);
    }

    @Transactional
    public Member uploadAvatar(String loginId, MultipartFile multipartFile) {
//        String currentLoginId = SecurityUtil.getCurrentUserLoginId().orElseThrow(IllegalStateException::new);
//        if (!currentLoginId.equals(loginId)) {
//            throw new CustomException(INVALID_MEMBER_PROFILE); // path 상의유저와 다른 유저가 요청
//        }
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(INVALID_MEMBER_NICKNAME));
        try {
            return fileService.storeProFile(multipartFile, member);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePassword(Member findMember, Map<String, String> password) {
        checkPassword(findMember, password.get("exPassword"));
        modifyPassword(findMember, password.get("newPassword"));
    }
}

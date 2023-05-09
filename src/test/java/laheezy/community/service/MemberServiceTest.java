package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.MemberRepository;
import laheezy.community.repository.jwt.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    private MemberRequestDto requestDto = new MemberRequestDto("PASS", "NAME", "NICKNAME", "EMAIL@naver.com");

    @Test
    void signup() {
        Member signup = memberService.signup(requestDto);

        assertThat(signup.getEmail()).isEqualTo("EMAIL@naver.com");
        assertThat(signup.getNickname()).isEqualTo("NICKNAME");
        assertThat(signup.getLoginId()).isEqualTo("NAME");
        assertTrue(passwordEncoder.matches("PASS", signup.getPassword()));
        //password는 encode 타임 마다 다르게 나와서 우선 pass 추후에 확인 필요
    }

    @Test
    void validateDuplicateNickname() {
        Member signup = memberService.signup(requestDto);
        //중복된 이름으로 회원가입 하는경우
        assertThrows(RuntimeException.class, () -> memberService.signup(requestDto));
    }


    @Test
    void findByNickname() {
        Member signup = memberService.signup(requestDto);
        Member byNickname = memberService.findByNickname(signup.getNickname());
        assertThat(signup).isEqualTo(byNickname);
    }


    @Test
    @DisplayName("비밀번호 일치를 확인한다.")
    void checkPassword() {
        Member signup = memberService.signup(requestDto);
        memberService.checkPassword(signup, "PASS");
        assertThrows(CustomException.class, () -> memberService.checkPassword(signup, "WRONGANS"));
    }


    @Test
    @DisplayName("로그아웃시에 리프레시 토큰 삭제 확인")
    void checkLogout() {
        Member signup = memberService.signup(requestDto);
        memberService.login(new LoginDto("NAME", "PASS"));
        memberService.logout(signup);

        assertTrue(refreshTokenRepository.findByKey(signup.getLoginId()).isEmpty());
    }

    @Test
    @DisplayName("회원 탈퇴시에 activate 를 false로 변경")
        //soft delete 테스트 domain에서 where삭제시 동작
    void deleteMember() {
        //given
        Member signup = memberService.signup(requestDto);
        assertTrue(signup.isActivated());
        //when
        memberService.deleteMember(signup);

        //then
        //assertFalse(memberRepository.findByLoginId(signup.getLoginId()).get().isActivated());
    }

    @Test
    @DisplayName("조회시에 activate = ture인 멤버만 조회")
    void findMember() {
        //given
        Member signup = memberService.signup(requestDto);
        for (int i = 0; i < 5; i++) {
            memberService.signup(new MemberRequestDto("pass" + i, "loginId" + i, "nick" + i, "email@go.c" + i));
        }
        assertThat(memberRepository.findAll().size()).isEqualTo(6);

        //when
        memberService.deleteMember(signup);

        //then
        assertThat(memberRepository.findAll().size()).isEqualTo(5);
    }

    @Test
    @DisplayName("만약 삭제된 객체를 조회하면 반환하지 않는다. ")
    void findOneMember() {
        //given
        Member signup = memberService.signup(requestDto);

        //when
        memberService.deleteMember(signup);

        //then
        assertThat(memberRepository.findById(signup.getId())).isEqualTo(Optional.empty());
    }
    //테스트시에는 새로운 객체를 기준으로 검사할것(객체 기준으로 하지 말자)

}
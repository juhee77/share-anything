package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.jwt.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void checkLogout(){
        Member signup = memberService.signup(requestDto);
        memberService.login(new LoginDto("NAME","PASS"));
        memberService.logout(signup);

        assertTrue(refreshTokenRepository.findByKey(signup.getLoginId()).isEmpty());
    }
}
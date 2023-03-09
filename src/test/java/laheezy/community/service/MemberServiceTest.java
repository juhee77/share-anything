package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.jwt.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private TokenProvider tokenProvider;

    private MemberRequestDto requestDto;

    @BeforeEach
    void setUp() {
        requestDto = new MemberRequestDto("PASS", "NAME", "NICKNAME", "EMAIL@naver.com");

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void signup() {
        Member signup = memberService.signup(requestDto);

        assertThat(signup.getEmail()).isEqualTo("EMAIL@naver.com");
        assertThat(signup.getName()).isEqualTo("NAME");
        assertThat(signup.getNickname()).isEqualTo("NICKNAME");
        //password는 encode 타임 마다 다르게 나와서 우선 pass 추후에 확인 필요
    }

    @Test
    void validateDuplicateNickname() {
        Member signup = memberService.signup(requestDto);
        assertThrows(RuntimeException.class, () -> memberService.signup(requestDto));
    }


    @Test
    void findByNickname() {
        Member signup = memberService.signup(requestDto);
        Member byNickname = memberService.findByNickname(signup.getNickname());
        assertThat(signup).isEqualTo(byNickname);
    }


    //TODO:: 중복값 에러 난다 확인
//    @Test
//    void testGetMemberWithAuthorities1() {
//        Member signup = memberService.signup(requestDto);
//        //TokenDto login = memberService.login(new LoginDto(signup.getNickname(), "PASS"));
//
//        Member byNickname = memberService.getMemberWithAuthorities(signup.getNickname()).get();
//    }
//
//    @Test
//    void testGetMemberWithAuthorities2() {
//        Member signup = memberService.signup(requestDto);
//
//        Member byNickname = memberService.findByNickname(signup.getNickname());
//        assertThat(signup).isEqualTo(byNickname);
//    }
}
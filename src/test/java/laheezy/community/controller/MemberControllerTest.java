package laheezy.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import laheezy.community.domain.Member;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
//@WebMvcTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//AutoConfigureMockMvc

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MemberControllerTest {
    //Mock객체 세팅

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 유저객체생성확인() throws Exception {
        //이름에 넘길 파라미터 네임
        MockMultipartFile file = new MockMultipartFile("profileImg", "image.jpg", "image/jpeg", "<<jeg data>>".getBytes(StandardCharsets.UTF_8));
        MemberRequestDto userMakeDto = new MemberRequestDto("password", "loginId", "nick", "bo@google.com", file);

        //when
        mockMvc.perform(multipart("/auth/signup")
                        .file(file)
                        .param("password", userMakeDto.getPassword())
                        .param("loginId", userMakeDto.getLoginId())
                        .param("nickname", userMakeDto.getNickname())
                        .param("email", userMakeDto.getEmail())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("loginId").value("loginId"))
                .andExpect(jsonPath("nickname").value("nick"))
                .andExpect(jsonPath("email").value("bo@google.com"));
        //then
        Member find = memberService.findByNickname("nick");
        assertTrue(passwordEncoder.matches("password", find.getPassword()));
        assertThat(find.getLoginId()).isEqualTo("loginId");
    }

    private Member member, admin;
    private TokenDto login, loginAdmin;

    @Test
    @DisplayName("adimin 이름 검색 확인")
    public void findByLogin확인() throws Exception {
        initMember();
        mockMvc.perform(get("/user/" + member.getLoginId())
                        .header("Authorization", "Bearer " + loginAdmin.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value(member.getNickname()))
                .andExpect(jsonPath("loginId").value(member.getLoginId()))
                .andExpect(jsonPath("email").value(member.getEmail()));
    }

    @Test
    @DisplayName("my profile 확인")
    public void myProfile확인() throws Exception {
        initMember();
        mockMvc.perform(get("/me-profile")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value(member.getNickname()))
                .andExpect(jsonPath("loginId").value(member.getLoginId()))
                .andExpect(jsonPath("email").value(member.getEmail()))
                .andDo(print());

    }


    @Test
    @DisplayName("nickname 변경 확인")
    public void nickname변경() throws Exception {
        //given
        initMember();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> nick = new HashMap<>();
        nick.put("nickname", "modified");
        String requestBody = objectMapper.writeValueAsString(nick);

        //when
        mockMvc.perform(post("/member/modify/nickname")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value("modified"))
                .andExpect(jsonPath("loginId").value(member.getLoginId()))
                .andExpect(jsonPath("email").value(member.getEmail()));

        //then
        Member find = memberService.findById(member.getId());
        assertThat(find.getNickname()).isEqualTo("modified");
    }

    @Test
    @DisplayName("password 변경 확인")
    public void password변경() throws Exception {
        //given
        initMember();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> pass = new HashMap<>();
        pass.put("exPassword", "pass");
        pass.put("newPassword", "newPass");
        String requestBody = objectMapper.writeValueAsString(pass);

        //when
        mockMvc.perform(post("/member/modify/password")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk());

        //then
        Member findMember = memberService.findById(member.getId());
        assertTrue(passwordEncoder.matches("newPass", findMember.getPassword()));
    }


    void initMember() {
        member = memberService.signup(new MemberRequestDto("pass", "name", "nick", "go1@go"));
        admin = memberService.signup(new MemberRequestDto("pass2", "name2", "nick2", "go2@go"));
        memberService.setAdmin(admin);
        login = memberService.login(new LoginDto("name", "pass"));
        loginAdmin = memberService.login(new LoginDto("name2", "pass2"));
    }

}
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
class MemberControllerTest {
    //Mock객체 세팅

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;

    @Test
    public void 유저객체생성확인() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MemberRequestDto userMakeDto = new MemberRequestDto("password", "test", "nick", "bo@google.com");
        String requestBody = objectMapper.writeValueAsString(userMakeDto);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("loginId").value("test"))
                .andExpect(jsonPath("nickname").value("nick"))
                .andExpect(jsonPath("email").value("bo@google.com"));
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
                .andExpect(jsonPath("email").value(member.getEmail()));

    }


    @Test
    @DisplayName("nickname 변경 확인")
    public void nickname변경() throws Exception {
        initMember();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> nick = new HashMap<>();
        nick.put("nickname","modified");
        String requestBody = objectMapper.writeValueAsString(nick);

        mockMvc.perform(post("/member/modify/nickname")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("nickname").value("modified"))
                .andExpect(jsonPath("loginId").value(member.getLoginId()))
                .andExpect(jsonPath("email").value(member.getEmail()));

    }

    void initMember() {
        member = memberService.signup(new MemberRequestDto("pass", "name", "nick", "go1@go"));
        admin = memberService.signup(new MemberRequestDto("pass2", "name2", "nick2", "go2@go"));
        memberService.setAdmin(admin);
        login = memberService.login(new LoginDto("name", "pass"));
        loginAdmin = memberService.login(new LoginDto("name2", "pass2"));
    }

}
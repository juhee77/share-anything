package laheezy.community.controller;

import laheezy.community.domain.Member;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//https://jhkimmm.tistory.com/31
class PostControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Test
    public void 포스트객체생성확인() throws Exception {
        Member member = makeTestUser();
        TokenDto login = memberService.login(new LoginDto("nick", "pass"));

        mockMvc.perform(post("/api/post/add")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("title", "title")
                        .param("text", "text")
                        .param("open", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("writerNickname").value(member.getNickname()))
                .andExpect(jsonPath("title").value("title"))
                .andExpect(jsonPath("text").value("text"))
                .andExpect(jsonPath("open").value("true"));

    }

    private Member makeTestUser() {
        return memberService.signup(new MemberRequestDto("pass", "name", "nick", "go@go"));
    }
}
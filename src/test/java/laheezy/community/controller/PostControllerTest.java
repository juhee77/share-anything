package laheezy.community.controller;

import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
//https://jhkimmm.tistory.com/31
class PostControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;

    @Test
    public void 포스트객체생성확인() throws Exception {
        Member member = makeTestUser();
        TokenDto login = memberService.login(new LoginDto("loginId", "pass"));

        mockMvc.perform(post("/api/post/add")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("title", "title")
                        .param("text", "text")
                        .param("open", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("loginId").value(member.getLoginId()))
                .andExpect(jsonPath("title").value("title"))
                .andExpect(jsonPath("text").value("text"))
                .andExpect(jsonPath("open").value("true"));
    }

    @Test
    public void 본인작성포스트확인() throws Exception {
        Member member = makeTestUser();
        for (int i = 0; i < 3; i++) {
            Post build = Post.builder().member(member).title("post" + i).isOpen(true).build();
            build.setMember(member);

            postService.writePost(build);
        }
        log.info("member: {}", member);

        TokenDto login = memberService.login(new LoginDto("loginId", "pass"));

        mockMvc.perform(get("/api/post/get-mypost")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", member.getPosts().get(0)).exists())
                .andExpect(jsonPath("$[1].title", member.getPosts().get(1)).exists())
                .andExpect(jsonPath("$[2].title", member.getPosts().get(2)).exists());
    }

    private Member makeTestUser() {
        return memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go"));
    }
}
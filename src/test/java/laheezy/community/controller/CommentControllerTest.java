package laheezy.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.RequestMakeCommentDto;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.repository.PostRepository;
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
class CommentControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostRepository postRepository;

    @Test
    public void 댓글객체생성확인() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Member member = makeTestUser();
        TokenDto login = memberService.login(new LoginDto("name", "pass"));

        Post post = makeTestPost(member);
        RequestMakeCommentDto commentDto = new RequestMakeCommentDto(post.getId(), "text", true);
        String requestBody = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(post("/api/comment/add")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("writerNickname").value(member.getLoginId()))
                .andExpect(jsonPath("postId").value(post.getId()))
                .andExpect(jsonPath("text").value("text"))
                .andExpect(jsonPath("open").value(true));

    }

    private Member makeTestUser() {
        return memberService.signup(new MemberRequestDto("pass", "name", "nick", "go@go.com"));
    }

    private Post makeTestPost(Member member) {
        Post post = Post.builder()
                .title("post title")
                .member(member).isOpen(true).build();

        return postRepository.save(post);
    }
}
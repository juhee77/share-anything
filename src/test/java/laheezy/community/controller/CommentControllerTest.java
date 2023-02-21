package laheezy.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.RequestMakeCommentDto;
import laheezy.community.repository.MemberRepository;
import laheezy.community.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
class CommentControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    public void 댓글객체생성확인() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Member member = makeTestUser();
        log.info("member={}", member.toString());
        Post post = makeTestPost(member);
        log.info("post={}", post.toString());
        RequestMakeCommentDto commentDto = new RequestMakeCommentDto("nick", post.getId(), "text", true);
        String requestBody = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(post("/api/comment-add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("writerNickname").value("nick"))
                .andExpect(jsonPath("postId").value(post.getId()))
                .andExpect(jsonPath("text").value("text"))
                .andExpect(jsonPath("open").value(true));

    }

    private Member makeTestUser() {
        Member member = Member.builder()
                .nickname("nick")
                .password("pass")
                .loginId("loginId")
                .name("name")
                .email("email")
                .build();

        memberRepository.save(member);
        return member;
    }

    private Post makeTestPost(Member member) {
        Post post = Post.builder()
                .title("post title")
                .member(member).build();

        postRepository.save(post);
        return post;
    }
}
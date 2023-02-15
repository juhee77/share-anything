package laheezy.community.controller;

import laheezy.community.domain.Member;
import laheezy.community.repository.MemberRepository;
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
    private MemberRepository memberRepository;
    @Test
    public void 포스트객체생성확인() throws Exception {
        Member member = makeTestUser();

        mockMvc.perform(post("/api/post-add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("writerNickname", member.getNickname())
                        .param("title", "title")
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("writerNickname").value(member.getNickname()))
                .andExpect(jsonPath("title").value("title"))
                .andExpect(jsonPath("text").value("text"));

    }

    private Member makeTestUser() {
        Member member = Member.makeUser("id", "gogo", "test", "test");
        memberRepository.save(member);
        return member;
    }
}
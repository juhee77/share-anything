package laheezy.community.controller;

import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.BoardService;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostHeartService;
import laheezy.community.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
@ActiveProfiles("test")
class PostHeartControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private PostHeartService postHeartService;

    Member member;
    TokenDto login;
    Board board;
    Post post;

    @Test
    public void testAddPostHeart() throws Exception {
        // Given

        // When
        mockMvc.perform(post("/post/{postId}/like", post.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("postId").value(post.getId()))
                .andExpect(jsonPath("memberName").value(member.getNickname()));

        // Then
        assertTrue(postHeartService.checkAlreadyHeart(member, post));
    }

    @Test
    public void testDeletePostHeart() throws Exception {
        // Given
        postHeartService.addHeart(member, post);
        assertTrue(postHeartService.checkAlreadyHeart(member, post));

        // When
        mockMvc.perform(delete("/post/{postId}/like", post.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        // Then
        assertFalse(postHeartService.checkAlreadyHeart(member, post));
    }

    @Test
    public void testCheckPostHeartOk() throws Exception {
        // Given
        postHeartService.addHeart(member, post);

        // When
        mockMvc.perform(get("/post/{postId}/like", post.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());
        // Then
        assertTrue(postHeartService.checkAlreadyHeart(member, post));
    }

    @Test
    public void testCheckPostHeartDelete() throws Exception {
        // Given
        postHeartService.addHeart(member, post);
        postHeartService.deleteHeart(member, post);

        //when
        mockMvc.perform(get("/post/{postId}/like", post.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(print());
        // Then
        assertFalse(postHeartService.checkAlreadyHeart(member, post));
    }

    @BeforeEach
    public void makeTestUser() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        login = memberService.login(new LoginDto("loginId", "pass"));
        board = boardService.makeBoard(Board.builder().name("test").active(true).build());
        post = postService.writePost(Post.builder().member(member).title("post").board(board).isOpen(false).build());
    }
}
package laheezy.community.controller;

import laheezy.community.domain.Board;
import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.*;
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
class CommentHeartControllerTest {
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
    private CommentService commentService;
    @Autowired
    private CommentHeartService commentHeartService;

    Member member;
    TokenDto login;
    Board board;
    Post post;
    Comment comment;

    @Test
    public void testAddCommentHeart() throws Exception {
        // Given

        // When
        mockMvc.perform(post("/comment/{commentId}/like", comment.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("commentId").value(comment.getId()))
                .andExpect(jsonPath("memberName").value(member.getNickname()));

        // Then
        assertTrue(commentHeartService.checkAlreadyHeart(member, comment));
    }

    @Test
    public void testDeleteCommentHeart() throws Exception {
        // Given
        commentHeartService.addHeart(member, comment);
        assertTrue(commentHeartService.checkAlreadyHeart(member, comment));

        // When
        mockMvc.perform(delete("/comment/{commentId}/like", comment.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        // Then
        assertFalse(commentHeartService.checkAlreadyHeart(member, comment));
    }

    @Test
    public void testCheckCommentHeartOk() throws Exception {
        // Given
        commentHeartService.addHeart(member, comment);


        // When
        mockMvc.perform(get("/comment/{commentId}/like", comment.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());
        // Then
        assertTrue(commentHeartService.checkAlreadyHeart(member, comment));

    }

    @Test
    public void testCheckCommentHeartDelete() throws Exception {
        // Given
        commentHeartService.addHeart(member, comment);
        commentHeartService.deleteHeart(member, comment);

        //when
        mockMvc.perform(get("/comment/{commentId}/like", comment.getId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("false"))
                .andDo(print());
        // Then
        assertFalse(commentHeartService.checkAlreadyHeart(member, comment));
    }

    @BeforeEach
    public void makeTestUser() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        login = memberService.login(new LoginDto("loginId", "pass"));
        board = boardService.makeBoard(Board.builder().name("test").active(true).build());
        post = postService.writePost(Post.builder().member(member).title("post").board(board).isOpen(false).build());
        comment = commentService.writeComment(new Comment(member, post, "comment", true));
    }
}
package laheezy.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import laheezy.community.domain.Board;
import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.comment.CommentRequestDto;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.BoardService;
import laheezy.community.service.CommentService;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class CommentControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private BoardService boardService;

    Member member;
    TokenDto login;
    Board board;
    Post post;


    @Test
    public void 댓글객체생성확인() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        CommentRequestDto commentDto = new CommentRequestDto(post.getId(), "text", true);
        String requestBody = objectMapper.writeValueAsString(commentDto);

        mockMvc.perform(post("/comment/add")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("writerNickname").value(member.getNickname()))
                .andExpect(jsonPath("postId").value(post.getId()))
                .andExpect(jsonPath("text").value("text"))
                .andExpect(jsonPath("open").value(true));

    }

    @Test
    public void 본인작성댓글확인() throws Exception {

        for (int i = 0; i < 3; i++) {
            commentService.writeComment(new Comment(member, post, "comment" + i, true));
        }

        mockMvc.perform(get("/comment/my")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text", member.getComments().get(0).getText()).exists())
                .andExpect(jsonPath("$[1].text", member.getComments().get(1).getText()).exists())
                .andExpect(jsonPath("$[2].text", member.getComments().get(2).getText()).exists());
    }

    @BeforeEach
    public void makeTestUser() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        login = memberService.login(new LoginDto("loginId", "pass"));
        board = boardService.makeBoard(Board.builder().name("test").active(true).build());
        post = postService.writePost(Post.builder().member(member).title("post").board(board).isOpen(false).build());

    }

}
package laheezy.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.dto.post.PostForm;
import laheezy.community.dto.post.PostModifyRequestForm;
import laheezy.community.service.BoardService;
import laheezy.community.service.FollowingService;
import laheezy.community.service.MemberService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
@ActiveProfiles("test")
class PostControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    private FollowingService followingService;
    @Autowired
    private BoardService boardService;

    private Member memberA, memberB;
    private TokenDto loginA, loginB;
    private Board board;
    private Post post;

    @BeforeEach
    public void initSetting() {
        initMember();
        initBoard();
        initPost();
    }
    @Test
    public void 포스트객체생성확인() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        PostForm postForm = new PostForm("title", "text", true, "test");

        mockMvc.perform(post("/post")
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(postForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("writer").value(memberA.getNickname()))
                .andExpect(jsonPath("title").value("title"))
                .andExpect(jsonPath("text").value("text"))
                .andExpect(jsonPath("open").value("true"));
    }

    @Test
    public void 포스트객체수정확인() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        PostModifyRequestForm postModifyRequestForm = new PostModifyRequestForm("modify", "modify", true, board.getName());
        mockMvc.perform(post("/post")
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(postModifyRequestForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("writer").value(memberA.getNickname()))
                .andExpect(jsonPath("title").value("modify"))
                .andExpect(jsonPath("text").value("modify"))
                .andExpect(jsonPath("open").value("true"));
    }

    @Test
    public void 본인작성포스트확인() throws Exception {

        for (int i = 0; i < 3; i++) {
            postService.writePost(Post.builder().member(memberA).title("post" + i).isOpen(true).board(board).build());
        }

        mockMvc.perform(get("/my/post")
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", memberA.getPosts().get(0).getTitle()).exists())
                .andExpect(jsonPath("$[1].title", memberA.getPosts().get(1).getTitle()).exists())
                .andExpect(jsonPath("$[2].title", memberA.getPosts().get(2).getTitle()).exists());
    }

    @Test
    public void 팔로잉하는사람의포스트확인() throws Exception {
        followingService.addFollowing(memberA, memberB);//memberA -> memberB를 팔로잉 한다.

        //비공개 포스트 하나 작성
        postService.writePost(Post.builder().member(memberB).title("post").isOpen(false).board(board).build());
        for (int i = 0; i < 3; i++) {
            //포스트 작성
            postService.writePost(Post.builder().member(memberB).title("post" + i).isOpen(true).board(board).build());
        }

        log.info("member check:{}", memberB);
        mockMvc.perform(get("/follow/post")
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(memberB.getPosts().get(1).getTitle()))
                .andExpect(jsonPath("$[1].title").value(memberB.getPosts().get(2).getTitle()))
                .andExpect(jsonPath("$[2].title").value(memberB.getPosts().get(3).getTitle()))
                .andDo(print());
    }


    @Test
    public void 원하는postId의게시글확인() throws Exception {
        mockMvc.perform(get("/post/" + post.getId())
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("postId").value(post.getId()))
                .andExpect(jsonPath("title").value("post"))
                .andExpect(jsonPath("heartCnt").value(0));
    }

    private void initBoard() {
        board = boardService.makeBoard(Board.builder().name("test").active(true).build());
    }

    void initMember() {
        memberA = memberService.signup(new MemberRequestDto("pass", "name", "nick", "go1@go"));
        memberB = memberService.signup(new MemberRequestDto("pass2", "name2", "nick2", "go2@go"));
        loginA = memberService.login(new LoginDto("name", "pass"));
        loginB = memberService.login(new LoginDto("name2", "pass2"));
    }

    void initPost(){
        post = postService.writePost(Post.builder().member(memberA).title("post").board(board).isOpen(false).build());
    }

}
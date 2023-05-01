package laheezy.community.controller;

import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.dto.board.BoardResponseDto;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.BoardService;
import laheezy.community.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class BoardControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardService boardService;

    Member member;
    TokenDto login;

    @BeforeEach
    public void makeTestUser() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        login = memberService.login(new LoginDto("loginId", "pass"));
    }

    @Test
    @DisplayName("보드 생성")
    void makeBoard() throws Exception {
        //given
        String name = "music";

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/board/" + name)
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("music"))
                .andExpect(jsonPath("openPostCnt").value(0));

        //then
        Board board = boardService.getBoardWithActive("music");
        assertThat(board.getName()).isEqualTo("music");
        assertThat(board.getMaker()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("모든 보드 확인")
    void findAllBoard() throws Exception {
        //given
        Board board1 = boardService.makeBoard(Board.builder().name("test1").active(true).build());
        Board board2 = boardService.makeBoard(Board.builder().name("test2").active(true).build());
        Board board3 = boardService.makeBoard(Board.builder().name("test3").active(true).build());

        //when
        mockMvc.perform(get("/board")
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("test1"))
                .andExpect(jsonPath("$[1].name").value("test2"))
                .andExpect(jsonPath("$[2].name").value("test3"));

        //then
        List<BoardResponseDto> allBoardWithActive = boardService.findAllBoardWithActive();
        assertThat(allBoardWithActive.size()).isEqualTo(3);
    }
}
package laheezy.community.controller;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.exception.CustomException;
import laheezy.community.service.ChatroomService;
import laheezy.community.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
@ActiveProfiles("test")
class ChatroomControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ChatroomService chatroomService;

    Member member, memberAdmin;
    TokenDto login, loginAdmin;


    @Test
    @DisplayName("[ROLE_USER]채팅방을 제거하는 경우 테스트 - user")
    public void deleteTest() throws Exception {
        //givnen
        String name = "test";
        Chatroom chatroom = chatroomService.createRoom(name, member);

        //when
        mockMvc.perform(delete("/chat/room/{roomId}", chatroom.getRoomId())
                        .header("Authorization", "Bearer " + login.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        //then
        //삭제되지 않아야 한다.
        assertThat(chatroomService.findRoomByRoomId(chatroom.getRoomId()).getId()).isEqualTo(chatroom.getId());
    }

    @Test
    @DisplayName("[ROLE_ADMIN]채팅방을 제거하는 경우 테스트 - admin")
    public void deleteTest2() throws Exception {
        //givnen
        log.info("ADMIN ERROR {}", memberAdmin.getAuthority());
        String name = "test";
        Chatroom chatroom = chatroomService.createRoom(name, member);
        log.info("저장된 방의 룸아이디 {}", chatroom.getRoomId());


        //when
        mockMvc.perform(delete("/chat/room/{roomId}", chatroom.getRoomId())
                        .header("Authorization", "Bearer " + loginAdmin.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        //삭제되어야 한다.
        Assertions.assertThrows(CustomException.class, () -> chatroomService.findRoomByRoomId(chatroom.getRoomId()).getId());

    }

    @BeforeEach
    public void makeTestUser() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        login = memberService.login(new LoginDto("loginId", "pass"));
        memberAdmin = memberService.signup(new MemberRequestDto("pass2", "loginId2", "nick2", "go2@go.com"));
        memberService.setAdmin(memberAdmin);
        loginAdmin = memberService.login(new LoginDto("loginId2", "pass2"));
    }
}
package laheezy.community.service;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.repository.ChatroomRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
class ChatroomServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    ChatroomService chatroomService;
    @Autowired
    ChatroomRepository chatroomRepository;

    Member member;


    @BeforeEach
    void initMember() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
    }

    @Test
    @DisplayName("채팅방을 삭제하는 테스트 ")
    public void test() {
        //given
        String name = "test";
        Chatroom room = chatroomService.createRoom(name, member);
        assertThat(chatroomRepository.findByRoomName(name).get().getRoomId()).isEqualTo(room.getRoomId());

        //when
        chatroomService.deleteRoom(room);

        //then
        assertThat(chatroomRepository.findByRoomName(name)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("채팅방을 생성하는 테스트 ")
    public void createTest() {
        //given
        String name = "test";

        //when
        Chatroom room = chatroomService.createRoom(name, member);

        //then
        assertThat(chatroomRepository.findByRoomName(name).get().getId()).isEqualTo(room.getId());
    }

    @Test
    @DisplayName("채팅방을 조회하는 테스트")
    public void searchTest() {
        //given
        String name = "test";
        Chatroom room = chatroomService.createRoom(name, member);
        assertThat(chatroomRepository.findByRoomName(name).get().getRoomId()).isEqualTo(room.getRoomId());

        //when
        Chatroom roomByRoomId = chatroomService.findRoomByRoomId(room.getRoomId());

        //then
        assertThat(roomByRoomId.getId()).isEqualTo(room.getId());

    }

}
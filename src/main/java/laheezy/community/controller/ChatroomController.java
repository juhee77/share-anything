package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.dto.chat.room.ChatRoomDetailDto;
import laheezy.community.service.ChatroomService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatroomController {
    private final ChatroomService chatService;
    private final MemberService memberService;

    @GetMapping("/room")
    @Operation(summary = "모든 채팅 방 리스트")
    public List<ChatRoomDetailDto> findAllRooms() {
        log.info("findAllRoom");
        return chatService.findAllRoomWithSubscriber();
    }

    //현재 사용하지 않는 메서드
    @GetMapping("/room/{roomId}")
    public ChatRoomDetailDto findRoomById(@PathVariable("roomId") String roomId) {
        log.info("findRoom: {}", roomId);
        return chatService.findRoomByRoomIdWithSubscribe(roomId);
    }

    @PostMapping("/room/{name}")
    @Operation(summary = "채팅 방 생성")
    public ChatRoomDetailDto createRoom(@PathVariable("name") String name) {
        log.info("createRoom : {}", name);
        chatService.checkingDuplicateRoom(name);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Chatroom room = chatService.createRoom(name, nowLogin);
        return new ChatRoomDetailDto().createRoom(room);
    }

    @DeleteMapping("/room/{roomId}")
    @Operation(summary = "채팅 방 삭제", description = "admin권한에서만 삭제가능하도록 한다. ")
    public void deleteRoom(@PathVariable("roomId") String roomId) {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        log.info("deleteRoom : {}방을 {}가 삭제하길 요청", roomId, nowLogin.getNickname());

        Chatroom chatRoom = chatService.findRoomByRoomId(roomId);
        chatService.deleteRoom(chatRoom);
    }

    @GetMapping("/room/enter/{roomId}")
    @Operation(summary = "채팅 방 입장")
    public ChatRoomDetailDto roomDetail(@PathVariable("roomId") String roomId) {
        log.info("findRoomDetail : {}", roomId);
        return chatService.findRoomByRoomIdWithSubscribe(roomId);
    }

}

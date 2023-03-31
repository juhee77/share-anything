package laheezy.community.controller;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.dto.chat.room.ChatRoomDetailDto;
import laheezy.community.service.ChatroomService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatroomController {
    private final ChatroomService chatService;
    private final MemberService memberService;

    @GetMapping("/rooms")
    public List<ChatRoomDetailDto> findAllRooms() {
        log.info("findAllRoom");
        List<Chatroom> allRoom = chatService.findAllRoom();
        return allRoom.stream().map(ChatRoomDetailDto::chatRoomToDto).collect(Collectors.toList());
    }

    @PostMapping("/room/create")
    public ChatRoomDetailDto createRoom(@RequestParam("name") String name) {
        log.info("createRoome : {}", name);
        chatService.checkingDuplicateRoom(name);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Chatroom room = chatService.createRoom(name, nowLogin);
        return ChatRoomDetailDto.chatRoomToDto(room);
    }

    @GetMapping("/room/enter/{roomId}")
    public ChatRoomDetailDto roomDetail(@PathVariable("roomId") String roomId) {
        log.info("findRoomDetail : {}", roomId);
        return ChatRoomDetailDto.chatRoomToDto(chatService.findRoomByRoomId(roomId));
    }

    @GetMapping("/room/{roomId}")
    public ChatRoomDetailDto findRoomById(@PathVariable("roomId") String roomId) {
        log.info("findRoom: {}", roomId);
        return ChatRoomDetailDto.chatRoomToDto(chatService.findRoomByRoomId(roomId));
    }

}

package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import laheezy.community.domain.Chatroom;
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
        return chatService.findAllRoomWithSubscriber();
    }

    //현재 사용하지 않는 메서드
    @GetMapping("/room/{roomId}")
    public ChatRoomDetailDto findRoomById(@PathVariable("roomId") String roomId) {
        return chatService.findRoomByRoomIdWithSubscribe(roomId);
    }

    @PostMapping("/room/{name}")
    @Operation(summary = "채팅 방 생성")
    public ChatRoomDetailDto createRoom(@PathVariable("name") String name) {
        chatService.checkingDuplicateRoom(name);
        return new ChatRoomDetailDto().createRoom(chatService.createRoom(name, memberService.getMemberWithAuthorities().orElseThrow(RuntimeException::new)));
    }

    @DeleteMapping("/room/{roomId}")
    @Operation(summary = "채팅 방 삭제", description = "admin권한에서만 삭제가능하도록 한다. ", tags = "admin")
    public void deleteRoom(@PathVariable("roomId") String roomId) {
        chatService.deleteRoom(chatService.findRoomByRoomId(roomId));
    }

    @GetMapping("/room/enter/{roomId}")
    @Operation(summary = "채팅 방 입장")
    public ChatRoomDetailDto roomDetail(@PathVariable("roomId") String roomId) {
        return chatService.findRoomByRoomIdWithSubscribe(roomId);
    }

}

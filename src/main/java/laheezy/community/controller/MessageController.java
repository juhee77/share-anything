package laheezy.community.controller;

import laheezy.community.domain.ChatData;
import laheezy.community.dto.chat.data.ChatDataRequestDto;
import laheezy.community.dto.chat.data.ChatDataResponseDto;
import laheezy.community.service.ChatDataService;
import laheezy.community.service.ChatRoomService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatDataService chatDataService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;


    //@SendTo("/sub/chat/room/{roomId}")

    @GetMapping("/pub/chat/enter")
    @MessageMapping("/pub/chat/enter")
    public void enter(ChatDataRequestDto message) {
        message.setMessage(message.getWriter() + "님이 입장 하셨습니다");

        List<ChatDataResponseDto> chatList = chatDataService.findAllChatByRoomId(message.getRoomId());
        if (chatList != null) {
            for (ChatDataResponseDto c : chatList) {
                message.setWriter(c.getWriter());
                message.setMessage(c.getMessage());
            }
        }
        sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        chatDataService.save(message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatDataRequestDto message) {
        sendingOperations.convertAndSend("/sub/chat/room" + message.getRoomId(), message);
        chatDataService.save(message);
    }
}

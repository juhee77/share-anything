package laheezy.community.controller;

import laheezy.community.dto.chat.data.ChatDataRequestDto;
import laheezy.community.service.ChatDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatDataService chatDataService;

//    @MessageMapping("/chat/enter") //유저가 보낸 메세지를 받는다.
//    public void enter(ChatDataRequestDto message) {
//        log.info("enter: {}",message.getWriter());
//        message.setMessage(message.getWriter() + "님이 입장 하셨습니다");
//
//        List<ChatDataResponseDto> chatList = chatDataService.findAllChatByRoomId(message.getRoomId());
//        if (chatList != null) {
//            for (ChatDataResponseDto c : chatList) {
//                message.setWriter(c.getWriter());
//                message.setMessage(c.getMessage());
//            }
//        }
//        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), message);
//        chatDataService.save(message);
//    }

    @MessageMapping(value = "/chat")
    public void message(ChatDataRequestDto message, SimpMessageHeaderAccessor accessor) {
        System.out.println(message);
        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), message);
        chatDataService.save(message);
    }
}

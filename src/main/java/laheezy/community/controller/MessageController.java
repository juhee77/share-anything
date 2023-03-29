package laheezy.community.controller;

import laheezy.community.dto.chat.data.ChatDataRequestDto;
import laheezy.community.dto.chat.data.ChatDataResponseDto;
import laheezy.community.service.ChatDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatDataService chatDataService;

    //pub
    @MessageMapping("/chat/enter") //유저가 보낸 메세지를 받는다.
    public void enter(ChatDataRequestDto message) {
        log.info("enter: {}", message.getWriter());
        message.setMessage(message.getWriter() + "님이 입장 하셨습니다");
        chatDataService.save(message);

        ChatDataResponseDto chatDataResponseDto = getChatDataResponseDto(message);
        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), chatDataResponseDto);
        chatDataService.save(message);
    }

    @NotNull
    private ChatDataResponseDto getChatDataResponseDto(ChatDataRequestDto message) {
        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        // 포맷 적용
        String formatedNow = now.format(formatter);

        return new ChatDataResponseDto(message.getRoomId(), message.getWriter(), message.getMessage(), formatedNow);
    }

    @MessageMapping("/chat/out") //유저가 보낸 메세지를 받는다.
    public void out(ChatDataRequestDto message) {
        log.info("out: {}", message.getWriter());
        message.setMessage(message.getWriter() + "님이 퇴장 하셨습니다");
        ChatDataResponseDto chatDataResponseDto = getChatDataResponseDto(message);

        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), chatDataResponseDto);
        chatDataService.save(message);
    }

    @MessageMapping(value = "/chat/send")
    public void message(ChatDataRequestDto message) {
        log.info("out: {}", message.getWriter());
        ChatDataResponseDto chatDataResponseDto = getChatDataResponseDto(message);

        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), chatDataResponseDto);
        chatDataService.save(message);
    }


    @MessageMapping(value = "/find/befor/message")
    public void findBeforeMessage(ChatDataRequestDto message) {
//        List<ChatDataResponseDto> chatList = chatDataService.findAllChatByRoomId(message.getRoomId());
//        if (chatList != null) {
//            for (ChatDataResponseDto c : chatList) {
//                message.setWriter(c.getWriter());
//                message.setMessage(c.getMessage());
//            }
//        }
    }

}

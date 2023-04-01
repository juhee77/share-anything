package laheezy.community.controller;

import laheezy.community.domain.*;
import laheezy.community.dto.chat.data.ChatDataRequestDto;
import laheezy.community.dto.chat.data.ChatDataResponseDto;
import laheezy.community.service.ChatdataService;
import laheezy.community.service.ChatroomService;
import laheezy.community.service.MemberChatroomService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatdataController {
    private final SimpMessageSendingOperations sendingOperations;
    private final MemberChatroomService memberChatRoomService;
    private final MemberService memberService;
    private final ChatdataService chatDataService;
    private final ChatroomService chatRoomService;

    //pub으로 받고 sub로 보낸다
    @Transactional
    @MessageMapping("/chat/enter")
    public void enter(ChatDataRequestDto message) {
        log.info("enter: {}", message);
        message.setMessage(message.getWriter() + "님이 입장 하셨습니다");

        Member member = memberService.findByNickname(message.getWriter());
        Chatroom room = chatRoomService.findRoomByRoomId(message.getRoomId());

        if (memberChatRoomService.checkMemberChatroom(member, room)) {
            // 해당 방의 이전 채팅 기록 가져오기
            List<ChatDataResponseDto> chatList = new ArrayList<>();
            MemberChatroom mcr = memberChatRoomService.findMemberChatroom(member, room);
            List<Chatdata> allChatList = chatDataService.findAllChatByRoomIdAndDate(room, mcr.getDate());
            if (allChatList != null) {
                chatList = allChatList.stream().map(this::convertToDto).collect(Collectors.toList());
            }

            // 해당 사용자에게만 메시지 전송
            //String destination = "/sub/chat/enter/" + message.getRoomId();
            //sendingOperations.convertAndSendToUser(member.getNickname(),destination, chatList);
            sendingOperations.convertAndSend("/user/" + message.getWriter() + "/sub/chat/enter/" + message.getRoomId(), chatList); //해당 url을 구독하고 있는 사람들에게 전송
            //sendingOperations.convertAndSend("/sub/chat/enter/" + message.getRoomId(), chatList);
            log.info("지난 기록 전송 완료: {}", chatList);
        } else {
            //구독 하도록
            log.info("구독 하였습니다.");
            memberChatRoomService.subscribe(member, room);

            // 입장 메시지 추가
            Chatdata save = chatDataService.save(message, MessageType.ENTER);
            ChatDataResponseDto nowEnter = convertToDto(save);
            sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), nowEnter);
        }

    }

    @NotNull
    private ChatDataResponseDto convertToDto(Chatdata chatData) {
        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        // 포맷 적용(후에 분리확인)
        String formatedNow = chatData.getDate().format(formatter);

        //현재 스트링 형태로 전송
        return new ChatDataResponseDto(chatData.getMessageType().toString(), chatData.getChatroom().getRoomId(), chatData.getWriter(), chatData.getMessage(), formatedNow);
    }

    //방을 나가는 경우 구독을 취소하는 경우
    @MessageMapping("/chat/out")
    public void out(ChatDataRequestDto message) {
        log.info("out: {}", message);
    }


    //프론트엔드에서 방을 나가는건지 아니면 그냥 이 방을 나간
    @MessageMapping("/chat/subscribe/out") //유저가 보낸 메세지를 받는다.
    public void subscribeOut(ChatDataRequestDto message) {
        log.info("out: {}", message);

        //방을 나가면 구독 자체를 취소하도록 한다.
        Member member = memberService.findByNickname(message.getWriter());
        Chatroom room = chatRoomService.findRoomByRoomId(message.getRoomId());

        //구독 취소
        memberChatRoomService.disSubscribe(member, room);

        //퇴장 문구
        message.setMessage(message.getWriter() + "님이 퇴장 하셨습니다");
        Chatdata save = chatDataService.save(message, MessageType.EXIT);
        ChatDataResponseDto chatDataResponseDto = convertToDto(save);
        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), chatDataResponseDto);
    }

    @MessageMapping(value = "/chat/send")
    public void message(ChatDataRequestDto message) {
        log.info("out: {}", message);
        Chatdata save = chatDataService.save(message, MessageType.BASIC);
        ChatDataResponseDto chatDataResponseDto = convertToDto(save);

        sendingOperations.convertAndSend("/sub/chat/" + message.getRoomId(), chatDataResponseDto);
    }

}

package laheezy.community.service;

import laheezy.community.domain.Chatdata;
import laheezy.community.domain.Chatroom;
import laheezy.community.dto.chat.data.ChatDataRequestDto;
import laheezy.community.repository.ChatdataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatdataService {
    private final ChatdataRepository chatDataRepository;
    private final ChatroomService chatRoomService;

    //메세지를 저장
    @Transactional
    public Chatdata save(ChatDataRequestDto message) {
        return chatDataRepository.save(Chatdata.toChatData(message, chatRoomService.findRoomByRoomId(message.getRoomId())));
    }

    public List<Chatdata> findAllChatByRoomIdAndDate(Chatroom chatRoom, LocalDateTime subscribeDateTime) {
        return chatDataRepository.findChatAllChatByDateAfterAndChatroom(subscribeDateTime,chatRoom);
    }

    public List<Chatdata> findAllChat(Chatroom chatroom) {
        return chatDataRepository.findAllById(chatroom.getId());
    }

    //본인이 소속되어 있는 채팅방을 로드한다.

}

package laheezy.community.service;

import laheezy.community.domain.ChatData;
import laheezy.community.domain.ChatRoom;
import laheezy.community.dto.chat.data.ChatDataRequestDto;
import laheezy.community.dto.chat.data.ChatDataResponseDto;
import laheezy.community.repository.ChatDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatDataService {
    private final ChatDataRepository chatDataRepository;
    private final ChatRoomService chatRoomService;

    //반환 메세지
    public List<ChatDataResponseDto> findAllChatByRoomId(String roomId) {
        log.info("ChatRoom : {}원함 ", roomId);
        ChatRoom byChatRoom = chatRoomService.findRoomByRoomId(roomId);

        //fetch lazy 에러, 직접 쿼리로 패치 조인해서 당경 와야함..
        List<ChatData> chatDataList = chatDataRepository.findAllChatByRoomId(byChatRoom);
        log.info("data size {}", chatDataList.size());
        List<ChatDataResponseDto> dtos = new ArrayList<>();
        for (ChatData chatData : chatDataList) {
            dtos.add(new ChatDataResponseDto(chatData.getChatroom().getRoomId(), chatData.getWriter(), chatData.getMessage()));
        }
        return dtos;
    }

    //메세지를 저장
    public void save(ChatDataRequestDto message) {
        chatDataRepository.save(ChatData.toChatData(message, chatRoomService.findRoomByRoomId(message.getRoomId())));
    }

    //본인이 소속되어 있는 채팅방을 로드한다.

}

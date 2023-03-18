package laheezy.community.service;

import laheezy.community.domain.ChatData;
import laheezy.community.domain.ChatRoom;
import laheezy.community.dto.chat.data.ChatDataRequestDto;
import laheezy.community.dto.chat.data.ChatDataResponseDto;
import laheezy.community.repository.ChatDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatDataService {
    private final ChatDataRepository chatDataRepository;
    private final ChatRoomService chatRoomService;

    //반환 메세지
    public List<ChatDataResponseDto> findAllChatByRoomId(String roomId) {
        Optional<ChatData> findByRoomId = chatDataRepository.findByChatroom(chatRoomService.findByChatRoom(roomId));
        return findByRoomId.stream().map(ChatDataResponseDto::toChatDataResponseDto).collect(Collectors.toList());
    }

    //메세지를 저장
    public void save(ChatDataRequestDto message) {
        chatDataRepository.save(ChatData.toChatData(message, chatRoomService.findByChatRoom(message.getRoomId())));
    }

    //본인이 소속되어 있는 채팅방을 로드한다.

}

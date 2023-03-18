package laheezy.community.service;

import laheezy.community.domain.ChatRoom;
import laheezy.community.domain.Member;
import laheezy.community.dto.chat.room.ChatRoomMakeDto;
import laheezy.community.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom findByChatRoom(String id) {
        return getRoomById(id);
    }

    @NotNull
    private ChatRoom getRoomById(String id) {
        Optional<ChatRoom> room = chatRoomRepository.findById(id);
        if (room.isPresent()) {
            return room.get();
        }
        throw new RequestRejectedException("없는 방 입니다");
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom createRoom(String name, Member member) {
        ChatRoomMakeDto dto = ChatRoomMakeDto.create(name);
        ChatRoom chatRoom = ChatRoom.toChatRoom(name, dto.getRoomId(), member.getNickname());
        return chatRoomRepository.save(chatRoom);
    }

}

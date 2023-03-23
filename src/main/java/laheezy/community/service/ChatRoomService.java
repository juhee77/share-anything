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

    public ChatRoom findRoomByRoomId(String id) {
        return getRoomByRoomId(id);
    }

    @NotNull
    private ChatRoom getRoomByRoomId(String id) {
        Optional<ChatRoom> room = chatRoomRepository.findByRoomId(id);

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

    public void checkingDuplicateRoom(String name) {
        if (chatRoomRepository.findByRoomName(name).isPresent())
            throw new RequestRejectedException("중복된 이름의 채팅방이 이미 존재합니다.");
    }
}

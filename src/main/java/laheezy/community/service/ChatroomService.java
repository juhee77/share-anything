package laheezy.community.service;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.dto.chat.room.ChatRoomDetailDto;
import laheezy.community.dto.chat.room.ChatRoomMakeDto;
import laheezy.community.repository.ChatRoomRepositoryImpl;
import laheezy.community.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatroomService {
    private final ChatroomRepository chatRoomRepository;
    private final ChatRoomRepositoryImpl chatRoomRepositoryImpl;

    public Chatroom findRoomByRoomId(String id) {
        return getRoomByRoomId(id);
    }

    public ChatRoomDetailDto findRoomByRoomIdWithSubscribe(String id) {
        return chatRoomRepositoryImpl.findRoomWithEnterSubscribeCnt(id);
    }

    @NotNull
    private Chatroom getRoomByRoomId(String id) {
        Optional<Chatroom> room = chatRoomRepository.findByRoomId(id);

        if (room.isPresent()) {
            return room.get();
        }
        throw new RequestRejectedException("없는 방 입니다");
    }

    public List<Chatroom> findAllRoom() {
        return chatRoomRepository.findAll();
    }


    public List<ChatRoomDetailDto> findAllRoomWithSubscriber() {
        return chatRoomRepositoryImpl.findAllRoomWithEnterSubscribeCnt();
    }

    @Transactional
    public Chatroom createRoom(String name, Member member) {
        ChatRoomMakeDto dto = ChatRoomMakeDto.create(name);
        Chatroom chatRoom = Chatroom.toChatRoom(name, dto.getRoomId(), member.getNickname());
        return chatRoomRepository.save(chatRoom);
    }

    public void checkingDuplicateRoom(String name) {
        if (chatRoomRepository.findByRoomName(name).isPresent())
            throw new RequestRejectedException("중복된 이름의 채팅방이 이미 존재합니다.");
    }

}

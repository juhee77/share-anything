package laheezy.community.service;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.dto.chat.room.ChatRoomDetailDto;
import laheezy.community.dto.chat.room.ChatRoomMakeDto;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.ChatRoomRepositoryImpl;
import laheezy.community.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static laheezy.community.exception.ErrorCode.DUPLICATION_CHATROOM_NAME;
import static laheezy.community.exception.ErrorCode.INVALID_CHATROOM_ID;

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
        throw new CustomException(INVALID_CHATROOM_ID);
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
            throw new CustomException(DUPLICATION_CHATROOM_NAME);
    }

}

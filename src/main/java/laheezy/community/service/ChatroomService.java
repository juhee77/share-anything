package laheezy.community.service;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.dto.chat.room.ChatRoomDetailDto;
import laheezy.community.dto.chat.room.ChatRoomMakeDto;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.ChatRoomRepositoryImpl;
import laheezy.community.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static laheezy.community.exception.ErrorCode.DUPLICATION_CHATROOM_NAME;
import static laheezy.community.exception.ErrorCode.INVALID_CHATROOM_ID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ChatroomService {
    private final ChatroomRepository chatRoomRepository;
    private final ChatRoomRepositoryImpl chatRoomRepositoryImpl;

    public Chatroom findRoomByRoomId(String roomId) {
        return getRoomByRoomId(roomId);
    }

    public ChatRoomDetailDto findRoomByRoomIdWithSubscribe(String id) {
        return chatRoomRepositoryImpl.findRoomWithEnterSubscribeCnt(id);
    }

    @NotNull
    private Chatroom getRoomByRoomId(String roomId) {
        Optional<Chatroom> room = chatRoomRepository.findByRoomId(roomId);

        if (room.isPresent()) {
            return room.get();
        }
        throw new CustomException(INVALID_CHATROOM_ID);
    }

    //현재 사용되지 않는 메서드 -> 현재 방의 구독자가 나오지 않는다 .
    public List<Chatroom> findAllRoom() {
        return chatRoomRepository.findAll();
    }


    //TODO : 구독자가 많은 순서대로, 가장 최근에 수정된 순서로 조회 순서 바꾸기
    public List<ChatRoomDetailDto> findAllRoomWithSubscriber() {
        return chatRoomRepositoryImpl.findAllRoomWithEnterSubscribeCnt();
    }

    @Transactional
    public Chatroom createRoom(String name, Member member) {
        ChatRoomMakeDto dto = new ChatRoomMakeDto().create(name);
        Chatroom chatRoom = Chatroom.toChatRoom(name, dto.getRoomId(), member.getNickname());
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void deleteRoom(Chatroom chatroom) {
        log.info("{} 방 제거. ", chatroom.getRoomId());
        chatRoomRepository.delete(chatroom);
    }

    public void checkingDuplicateRoom(String name) {
        if (chatRoomRepository.findByRoomName(name).isPresent())
            throw new CustomException(DUPLICATION_CHATROOM_NAME);
    }

}

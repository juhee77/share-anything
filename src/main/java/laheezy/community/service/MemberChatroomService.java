package laheezy.community.service;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.domain.MemberChatroom;
import laheezy.community.repository.MemberChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberChatroomService {
    private final MemberChatroomRepository memberChatRoomRepository;

    @Transactional
    public MemberChatroom subscribe(Member member, Chatroom room) {
        return memberChatRoomRepository.save(new MemberChatroom(member, room));
    }

    @Transactional
    public void disSubscribe(Member member, Chatroom room) {
        memberChatRoomRepository.deleteById(findMemberChatroom(member, room).getId());
    }

    public MemberChatroom findMemberChatroom(Member member, Chatroom room) {
        Optional<MemberChatroom> memberChatRoom = memberChatRoomRepository.findByMemberAndChatroom(member, room);
        if (memberChatRoom.isPresent()) {
            return memberChatRoom.get();
        }
        throw new IllegalArgumentException("해당 채팅방을 구독하고 있지 않습니다.");
    }

    public boolean checkMemberChatroom(Member member, Chatroom room) {
        if (memberChatRoomRepository.findByMemberAndChatroom(member, room).isPresent())
            return true;
        return false;
    }


}

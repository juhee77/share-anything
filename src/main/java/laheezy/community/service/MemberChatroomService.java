package laheezy.community.service;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.domain.MemberChatroom;
import laheezy.community.exception.CustomException;
import laheezy.community.repository.MemberChatroomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static laheezy.community.exception.ErrorCode.INVALID_SUBSCRIBE;

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
        MemberChatroom memberChatroom = findMemberChatroom(member, room);
        memberChatroom.delete(memberChatroom);
        memberChatRoomRepository.deleteById(memberChatroom.getId());
    }

    public MemberChatroom findMemberChatroom(Member member, Chatroom room) {
        Optional<MemberChatroom> memberChatRoom = memberChatRoomRepository.findByMemberAndChatroom(member, room);
        if (memberChatRoom.isPresent()) {
            return memberChatRoom.get();
        }
        throw new CustomException(INVALID_SUBSCRIBE);
    }

    public boolean checkMemberChatroom(Member member, Chatroom room) {
        return memberChatRoomRepository.findByMemberAndChatroom(member, room).isPresent();
    }


}

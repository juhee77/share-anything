package laheezy.community.repository;

import laheezy.community.domain.Chatroom;
import laheezy.community.domain.Member;
import laheezy.community.domain.MemberChatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberChatroomRepository extends JpaRepository<MemberChatroom, Long> , QuerydslPredicateExecutor<MemberChatroom> {
    Optional<MemberChatroom> findByMemberAndChatroom(Member member, Chatroom chatRoom);
}

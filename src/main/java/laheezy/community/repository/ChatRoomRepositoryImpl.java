package laheezy.community.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import laheezy.community.domain.Chatroom;
import laheezy.community.dto.chat.room.ChatRoomDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static laheezy.community.domain.QChatroom.chatroom;
import static laheezy.community.domain.QMemberChatroom.memberChatroom;

@Repository
@Slf4j
public class ChatRoomRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public ChatRoomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Chatroom.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<ChatRoomDetailDto> findAllRoomWithEnterSubscribeCnt() {
        log.info("Chatroom Query");
        List<Tuple> results = jpaQueryFactory
                .select(chatroom, memberChatroom.id.count())
                .from(chatroom)
                .leftJoin(chatroom.nowSubscriber, memberChatroom)
                .groupBy(chatroom)
                .fetch();

        return results.stream().map(tuple -> {
            Chatroom c = tuple.get(chatroom);
            long subscriberCnt = tuple.get(memberChatroom.id.count());
            return new ChatRoomDetailDto(c.getRoomId(), c.getRoomName(), c.getWriter(), (int) subscriberCnt);
        }).collect(Collectors.toList());
    }


    public ChatRoomDetailDto findRoomWithEnterSubscribeCnt(String roomId) {
        log.info("Chatroom Query with ID");
        List<Tuple> results = jpaQueryFactory
                .select(chatroom, memberChatroom.chatroom.count())
                .from(chatroom)
                .leftJoin(chatroom.nowSubscriber, memberChatroom)
                .where(chatroom.roomId.eq(roomId))
                .groupBy(chatroom)
                .fetch();

        Chatroom c = results.get(0).get(chatroom);
        long subscriberCnt = results.get(0).get(memberChatroom.chatroom.count());
        return new ChatRoomDetailDto(c.getRoomId(), c.getRoomName(), c.getWriter(), (int) subscriberCnt);

    }
}

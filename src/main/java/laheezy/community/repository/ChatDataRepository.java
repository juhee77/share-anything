package laheezy.community.repository;

import laheezy.community.domain.ChatData;
import laheezy.community.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatDataRepository extends JpaRepository<ChatData, Long> {
    Optional<ChatData> findByChatroom(ChatRoom room);

    @Query("SELECT c FROM ChatData c JOIN FETCH c.chatroom WHERE c.chatroom= :cr")
    List<ChatData> findAllChatByRoomId(@Param("cr") ChatRoom cr);
}

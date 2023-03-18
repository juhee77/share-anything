package laheezy.community.repository;

import laheezy.community.domain.ChatData;
import laheezy.community.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatDataRepository extends JpaRepository<ChatData,Long> {
    Optional<ChatData> findByChatroom(ChatRoom room);
}

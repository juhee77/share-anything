package laheezy.community.repository;

import laheezy.community.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByRoomId(String rooId);

    Optional<ChatRoom> findByRoomName(String roomName);
}

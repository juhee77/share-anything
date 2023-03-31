package laheezy.community.repository;

import laheezy.community.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, String> {
    Optional<Chatroom> findByRoomId(String rooId);

    Optional<Chatroom> findByRoomName(String roomName);
}

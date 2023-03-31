package laheezy.community.repository;

import laheezy.community.domain.Chatdata;
import laheezy.community.domain.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatdataRepository extends JpaRepository<Chatdata, Long> {
    List<Chatdata> findChatAllChatByDateAndChatroom(Chatroom chatroom, LocalDateTime date);
}

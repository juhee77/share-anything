package laheezy.community.dto.chat.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomMakeDto { //방을 만든다
    private String roomId;
    private String name;

    public static ChatRoomMakeDto create(String name) {
        ChatRoomMakeDto room = new ChatRoomMakeDto();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}

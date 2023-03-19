package laheezy.community.dto.chat.room;

import laheezy.community.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDto { //방을 만든다
    private Long id; //entity의 기본키(데이터 베이스키)
    private String roomId; //만들어진 키
    private String name;
    private String writer;

    public static ChatRoomDetailDto chatRoomToDto(ChatRoom chatRoom) {
        ChatRoomDetailDto roomResponseDto = new ChatRoomDetailDto();

        roomResponseDto.setId(chatRoom.getId());
        roomResponseDto.setWriter(chatRoom.getWriter());
        roomResponseDto.setRoomId(chatRoom.getRoomId());
        roomResponseDto.setName(chatRoom.getRoomName());
        return roomResponseDto;
    }
}

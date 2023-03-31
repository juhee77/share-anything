package laheezy.community.dto.chat.room;

import laheezy.community.domain.Chatroom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailDto { //방을 만든다
    private String id; //만들어진 키
    private String name;
    private String writer;

    public static ChatRoomDetailDto chatRoomToDto(Chatroom chatRoom) {
        ChatRoomDetailDto roomResponseDto = new ChatRoomDetailDto();

        roomResponseDto.setWriter(chatRoom.getWriter());
        roomResponseDto.setId(chatRoom.getRoomId());
        roomResponseDto.setName(chatRoom.getRoomName());
        return roomResponseDto;
    }
}

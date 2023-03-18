package laheezy.community.dto.chat.data;

import laheezy.community.domain.ChatData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDataResponseDto {
    private Long chatId;
    private Long chatRoomId;
    private String roomId;
    private String writer;
    private String message;

    public static ChatDataResponseDto toChatDataResponseDto(ChatData chatData) {
        ChatDataResponseDto chatDataResponseDto = new ChatDataResponseDto();
        //data
        chatDataResponseDto.setChatId(chatData.getId());
        //room
        chatDataResponseDto.setChatRoomId(chatData.getChatroom().getId());
        chatDataResponseDto.setRoomId(chatData.getChatroom().getRoomId());

        chatDataResponseDto.setWriter(chatData.getWriter());
        chatDataResponseDto.setMessage(chatDataResponseDto.getMessage());
        return chatDataResponseDto;
    }
}

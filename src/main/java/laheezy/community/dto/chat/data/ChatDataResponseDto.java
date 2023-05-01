package laheezy.community.dto.chat.data;

import laheezy.community.domain.Chatdata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDataResponseDto {
    private String messageType;
    private String roomId;
    private String writer;
    private String message;
    private String time;

    public static ChatDataResponseDto convertToDto(Chatdata chatData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        String formatedNow = chatData.getDate().format(formatter);
        return new ChatDataResponseDto(chatData.getMessageType().toString(), chatData.getChatroom().getRoomId(), chatData.getWriter(), chatData.getMessage(), formatedNow);
    }

}

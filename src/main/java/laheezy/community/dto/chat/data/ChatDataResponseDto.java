package laheezy.community.dto.chat.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDataResponseDto {
    private String messageType;
    private String roomId;
    private String writer;
    private String message;
    private String time;
}

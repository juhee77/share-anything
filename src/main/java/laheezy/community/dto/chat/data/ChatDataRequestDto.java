package laheezy.community.dto.chat.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatDataRequestDto {
    private String roomId;// 방 번호
    private String writer; // 채팅 송신자
    private String message;// 메세지
}

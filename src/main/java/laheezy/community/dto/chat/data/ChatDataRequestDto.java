package laheezy.community.dto.chat.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDataRequestDto {
    private String roomId;// 방 번호
    private String writer; // 채팅 송신자
    private String message;// 메세지
}

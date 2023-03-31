package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import laheezy.community.dto.chat.data.ChatDataRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Schema(description = "채팅 내용")
public class Chatdata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    private String writer;
    private String message;

    @CreationTimestamp
    private LocalDateTime date;

    //생성자
    public static Chatdata toChatData(ChatDataRequestDto chatDataDto, Chatroom chatRoom) {
        Chatdata chatData = new Chatdata();
        chatData.chatroom = chatRoom;
        chatData.message = chatDataDto.getMessage();
        chatData.writer = chatDataDto.getWriter();
        return chatData;
    }
}

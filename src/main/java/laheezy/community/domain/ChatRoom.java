package laheezy.community.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @Column(unique = true)
    private String roomId;//uuid
    private String roomName;//채팅방 이름
    private String writer;//생성한 사람 이름

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatroom") //연관관계의 주인을 지정
    private List<ChatData> chatDataList = new ArrayList<>();


    //생성자
    public static ChatRoom toChatRoom(String roomName, String roomId, String writer) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomName = roomName;
        chatRoom.roomId = roomId;
        chatRoom.writer = writer;
        return chatRoom;
    }

}

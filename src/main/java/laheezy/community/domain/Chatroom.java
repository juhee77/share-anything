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
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @Column(unique = true)
    private String roomId;//uuid
    private String roomName;//채팅방 이름
    private String writer;//생성한 사람 이름

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatroom", cascade = CascadeType.ALL, orphanRemoval = true)
    //연관관계의 주인을 지정
    private List<Chatdata> chatDataList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chatroom")
    private List<MemberChatroom> nowSubscriber = new ArrayList<>();

    //생성자
    public static Chatroom toChatRoom(String roomName, String roomId, String writer) {
        Chatroom chatRoom = new Chatroom();
        chatRoom.roomName = roomName;
        chatRoom.roomId = roomId;
        chatRoom.writer = writer;
        return chatRoom;
    }

}

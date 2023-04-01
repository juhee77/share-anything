package laheezy.community.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Schema(description = "입장한 방") //관계 테이블
public class MemberChatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    @CreationTimestamp
    private LocalDateTime date;

    private void setMember(Member member) {
        member.getMemberChatRooms().add(this);
        this.member = member;
    }

    private void setChatroom(Chatroom chatRoom) {
        chatRoom.getNowSubscriber().add(this);
        this.chatroom = chatRoom;
    }

    public MemberChatroom(Member member, Chatroom chatRoom) {
        setMember(member);
        setChatroom(chatRoom);
    }
}

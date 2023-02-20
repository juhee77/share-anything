package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Schema(description = "게시글")
public class Post {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    @Schema(description = "ID")
    @Column(name = "post_id") //게시글의 Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //게시글 작성자


    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>(); //게시물의 댓글


    private boolean isOpen; //공개 비공개
    private int heart; // 좋아요
    private int view; // 조회수

    private String title;
    private String text;
    private LocalDateTime writeDate;

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    //생성 메서드
    @Builder
    public Post(Member member, String title, String text, boolean isOpen) {
        setMember(member);
        this.title = title;
        this.text = text;
        this.writeDate = LocalDateTime.now();
        this.isOpen=isOpen;
        this.heart=0;
        this.view=0;
    }
}

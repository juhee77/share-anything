package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Schema(description = "댓글")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID")
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //댓글 작성자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post; //댓글이 작성된 글

    private String text; //댓글내용
    private boolean isOpen; //공개 비공개
    private int heart; // 좋아요
    private LocalDateTime writeDate; //댓글이 작성된 시간

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getComments().add(this);
    }

    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this);
    }

    //생성 메서드
    @Builder
    public Comment(Member member, Post post, String text, boolean isOpen) {
        setMember(member);
        setPost(post);
        this.text = text;
        this.writeDate = LocalDateTime.now();
        this.isOpen = isOpen;
        this.heart = 0;
    }
}

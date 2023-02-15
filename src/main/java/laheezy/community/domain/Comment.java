package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@NoArgsConstructor
@Data
@Entity
@Schema(description = "댓글")
public class Comment {
    @Id
    @GeneratedValue
    @Schema(description = "ID")
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //댓글 작성자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post; //댓글이 작성된 글

    private LocalDateTime writeDate; //댓글이 작성된 시간

    private String text; //댓글

    //게시글의 잠금기능


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
    public static Comment createComment( Member member,Post post,String text) {
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setPost(post);
        comment.setText(text);
        comment.setWriteDate(LocalDateTime.now());
        return comment;
    }
}

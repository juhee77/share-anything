package laheezy.community.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Schema(description = "댓글 좋아요 기능")
public class CommentHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Comment_id")
    private Comment comment;

    private LocalDateTime date = LocalDateTime.now();

    public void setComment(Comment comment) {
        this.comment = comment;
        if (!comment.getCommentHearts().contains(this)) {
            comment.getCommentHearts().add(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getCommentHearts().contains(this)) {
            member.getCommentHearts().add(this);
        }
    }

    public CommentHeart(Member member, Comment Comment) {
        setMember(member);
        setComment(Comment);
    }

    public void delete() {
        this.member.getCommentHearts().remove(this);
        this.comment.getCommentHearts().remove(this);
    }
}

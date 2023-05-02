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
@Schema(description = "포스트 좋아요 기능")
public class PostHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @CreationTimestamp
    private LocalDateTime date;

    //이렇게 다 양방향 관계로 하는게 맞는걸까.
    //연관관계 메서드
    public void setPost(Post post) {
        this.post = post;
        if (!post.getPostHearts().contains(this)) {
            post.getPostHearts().add(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        if (!member.getPostHearts().contains(this)) {
            member.getPostHearts().add(this);
        }
    }

    public PostHeart(Member member, Post post) {
        setMember(member);
        setPost(post);
    }

    public void delete() {
        this.member.getPostHearts().remove(this);
        this.post.getPostHearts().remove(this);
    }
}

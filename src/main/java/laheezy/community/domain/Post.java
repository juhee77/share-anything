package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Schema(description = "게시글")
public class Post {
    @Id
    @GeneratedValue
    @Schema(description = "ID")
    @Column(name = "post_id") //게시글의 Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //게시글 작성자


    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>(); //게시물의 댓글

    private String title;
    private String text;
    private LocalDateTime writeDate;

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    //생성 메서드
    public static Post createPost( Member member,String title, String text) {
        Post post = new Post();
        post.setMember(member);
        post.setTitle(title);
        post.setText(text);
        post.setWriteDate(LocalDateTime.now());
        return post;
    }
}

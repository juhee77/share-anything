package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Schema(description = "게시글")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "ID")
    @Column(name = "post_id") //게시글의 Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //게시글 작성자

    @Builder.Default
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>(); //게시물의 댓글

    private boolean isOpen; //공개 비공개

    @Builder.Default
    private long view = 0; // 조회수

    private String title;
    private String text;
    @Builder.Default
    private LocalDateTime writeDate = LocalDateTime.now();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostHeart> postHearts = new ArrayList<>();

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", member=" + member +
                ", isOpen=" + isOpen +
                ", view=" + view +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", writeDate=" + writeDate +
                '}';
    }


}

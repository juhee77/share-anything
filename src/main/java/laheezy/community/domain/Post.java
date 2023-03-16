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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Schema(description = "게시글")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "게시글의 ID")
    @Column(name = "post_id") //게시글의 Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Schema(description = "게시글의 작성자")
    private Member member; //게시글 작성자

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Schema(description = "게시글의 댓글들")
    private List<Comment> comments = new ArrayList<>(); //게시물의 댓글

    @Schema(description = "게시글의 공개/비공개")
    private boolean isOpen;

    @Schema(description = "게시글의 조회수")
    private long view = 0; // 조회수

    @Schema(description = "게시글의 제목")
    private String title;
    @Schema(description = "게시글의 내용")
    private String text;

    @Schema(description = "게시글의 작성날짜")
    private LocalDateTime writeDate = LocalDateTime.now();
    @Schema(description = "게시글의 수정날짜")
    private LocalDateTime lastModifiedTime;

    @Schema(description = "게시글의 좋아요")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostHeart> postHearts = new ArrayList<>();

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }

    @Builder
    public Post(Long id, Member member, boolean isOpen, long view, String title, String text, LocalDateTime writeDate, LocalDateTime lastModifiedTime) {
        setMember(member);
        this.id = id;
        this.isOpen = isOpen;
        this.view = view;
        this.title = title;
        this.text = text;
        this.writeDate = LocalDateTime.now();
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

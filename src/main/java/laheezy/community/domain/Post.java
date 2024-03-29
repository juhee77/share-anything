package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import laheezy.community.domain.file.Postfile;
import laheezy.community.dto.post.PostModifyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    @Column(name = "post_id") //게시글의 Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //게시글 작성자

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>(); //게시물의 댓글

    private boolean isOpen;

    private long view = 0; // 조회수

    private String title;
    private String text;

    @CreationTimestamp
    private LocalDateTime writeDate;
    @UpdateTimestamp
    private LocalDateTime lastModifiedTime;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostHeart> postHearts = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postfile> postfiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    Board board;

    //연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }


    public void setBoard(Board board) {
        this.board = board;
        board.getPosts().add(this);
    }

    @Builder
    public Post(Long id, Member member, Board board, boolean isOpen, long view, String title, String text) {
        setMember(member);
        setBoard(board);
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


    public void modify(PostModifyDto postModifyRequestForm) {
        this.text = postModifyRequestForm.getText();
        this.isOpen = postModifyRequestForm.isOpen();
        this.title = postModifyRequestForm.getTitle();
        this.setBoard(postModifyRequestForm.getBoard());
    }
}

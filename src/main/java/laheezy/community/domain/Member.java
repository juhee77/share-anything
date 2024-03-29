package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import laheezy.community.domain.file.Profile;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Schema(description = "유저")
@Builder
@SQLDelete(sql = "UPDATE member SET activated = false WHERE member_id = ?")
@Where(clause = "activated = true")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID")
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;
    @Column(nullable = false, unique = true)
    private String loginId;//loginId대신 사용 (중복 안되도록 설계한다)(영어로만)

    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default
    @OneToOne(fetch = LAZY, mappedBy = "member")
    private Profile profileImage = null;

    @CreationTimestamp
    private LocalDateTime joinDate;
    @UpdateTimestamp
    private LocalDateTime lastModified;

    private boolean activated; //삭제된 user인가(회원 탈퇴)

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "member")
    @Schema(description = "유저의 게시글")
    private List<Post> posts = new ArrayList<>();

    @Builder.Default //builder로 선언 하는 경우 없는값이라고 null을 넣는다 따라서 이렇게 해야함
    @OneToMany(fetch = LAZY, mappedBy = "member")
    @Schema(description = "댓글")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<PostHeart> postHearts = new ArrayList<>();


    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<CommentHeart> commentHearts = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "memberA", cascade = CascadeType.ALL)
    private List<Following> following = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "memberB", cascade = CascadeType.ALL)
    private List<Following> follower = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberChatroom> memberChatRooms = new ArrayList<>();

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", joinDate=" + joinDate +
                ", Post=" + posts.size() +
                '}';
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
        this.lastModified = LocalDateTime.now();
    }

    public void setAdmin() {
        this.authority = Authority.ROLE_ADMIN;
    }

    public void modifyPassword(String newPassword) {
        this.password = newPassword;
    }

    public void setProfile(Profile file) {
        profileImage = file;
    }

}

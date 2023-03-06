package laheezy.community.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Schema(description = "유저")
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID")
    @Column(name = "member_id")
    private Long id;

    private String name;
    @Column(nullable = false, unique = true)
    private String nickname;//loginId대신 사용 (중복 안되도록 설계한다)(영어로만)

    @JsonIgnore
    private String password;
    private String email;
    private final LocalDateTime joinDate = LocalDateTime.now();

    @JsonIgnore
    private boolean activated;

    @ManyToMany
    @JoinTable(name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @OneToMany(fetch = LAZY, mappedBy = "member")
    @Schema(description = "유저의 게시글")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "member")
    @Schema(description = "댓글")
    private List<Comment> comments = new ArrayList<>();

    /*연관관계 주인*/
    @ManyToOne
    @JoinColumn
    @JsonIgnore //순환 참조 문제 -> [CHECK] 테이블 분리 고려
    private Member follower = this;

    /*연관관계 주인*/
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Member following = this;

    @JsonIgnore
    @OneToMany(fetch = LAZY, mappedBy = "following", cascade = CascadeType.ALL)
    private List<Member> followings = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = LAZY, mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Member> followers = new ArrayList<>();


    //연관관계 매핑
    public void addFollowingUser(Member following) {
        this.followings.add(following);

        if (!following.getFollowers().contains(this)) {
            following.getFollowers().add(this);
        }
        //연관관계의 주인을 통한 확인
        if (!following.getFollower().getFollowers().contains(this)) {
            following.getFollower().getFollowers().add(this);
        }
    }

    public void addFollowerUser(Member follower) {
        this.followers.add(follower);

        if (follower.getFollowings().contains(this)) {
            follower.getFollowings().add(this);
        }
        //연관관계의 주인을 통한 확인
        if (!follower.getFollowing().getFollowings().contains(this)) {
            follower.getFollowing().getFollowings().add(this);
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }

}

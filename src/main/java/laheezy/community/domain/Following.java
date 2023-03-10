package laheezy.community.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
@Schema(description = "팔로우를 관리한다 ") //A->B를 팔로우한다.
public class Following {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "follower_id")
    private Member memberA;

    @JsonIgnore
    @ManyToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "following_id") //둘중 한곳에만 추가
    private Member memberB;

    //연관관계 메서드
    public void setFollowing(Member memberA) {
        this.memberA = memberA;
        if (!memberA.getFollowing().contains(this)) {
            memberA.getFollowing().add(this);
        }
    }

    public void setFollower(Member memberB) {
        this.memberB = memberB;
        if (!memberB.getFollower().contains(this)) {
            memberB.getFollower().add(this);
        }
    }

    public Following(Member memberA, Member memberB) {
        setFollowing(memberA);
        setFollower(memberB);
    }

}

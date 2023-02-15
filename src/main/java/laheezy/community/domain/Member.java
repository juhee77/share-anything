package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

//import javax.persistence.*;
//import javax.persistence.Entity;

@NoArgsConstructor
@Data
@Entity
@Schema(description = "유저")
public class Member {
    @Id
    @GeneratedValue
    @Schema(description = "ID")
    @Column(name = "member_id")
    private Long id;

    private String loginId;

    private String name;

    private String nickname;

    private String password;

    @OneToMany(fetch = LAZY,mappedBy = "member")
    @Schema(description = "유저의 게시글")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(fetch = LAZY,mappedBy = "member")
    @Schema(description = "댓글")
    private List<Comment> comments = new ArrayList<>();

    //생성 메서드
    public static Member makeUser(String loginId, String password, String name, String nickname) {
        Member member = new Member();
        member.setName(name);
        member.setLoginId(loginId);
        member.setNickname(nickname);
        member.setPassword(password);
        return member;
    }



}

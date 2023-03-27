package laheezy.community.service;

import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.member.MemberRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    private Member member;
    private Post post;

    @BeforeEach
    void initMember() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        post = postService.writePost(Post.builder().member(member).title("post").isOpen(false).build());
    }


    @Test
    @DisplayName("댓글 생성 확인")
    void writeComment() {
        //given
        Comment test = Comment.builder().text("TEST").member(member).post(post).build();
        //when
        commentService.writeComment(test);
        //then
        Assertions.assertThat(test).isEqualTo(commentService.findById(test.getId()));
    }

    @Test
    @DisplayName("댓글 삭제 확인")
    public void findByCommendId() {
        //given
        Comment test = Comment.builder().text("TEST").member(member).post(post).build();
        commentService.writeComment(test);
        //when
        commentService.removeComment(test.getId());
        //then
        assertThrows(RuntimeException.class, () -> commentService.findById(test.getId()));
    }

}
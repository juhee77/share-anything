package laheezy.community.service;

import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.member.MemberRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CommentHeartServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentHeartService commentHeartService;

    private Member member;
    private Post post;
    private Comment comment;

    @BeforeEach
    void initMember() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        post = postService.writePost(Post.builder().member(member).title("post").isOpen(false).build());
        comment = commentService.writeComment(Comment.builder().text("TEST").member(member).post(post).build());
    }

    @Test
    public void 하트추가() {
        commentHeartService.addHeart(member, comment);
        Assertions.assertTrue(commentHeartService.checkAlreadyHeart(member, comment));
    }

    @Test
    public void 하트삭제() {
        commentHeartService.addHeart(member, comment);
        commentHeartService.deleteHeart(member, comment);
        Assertions.assertTrue(commentHeartService.checkAlreadyHeart(member, comment));
    }

}
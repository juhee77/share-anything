package laheezy.community.service;

import laheezy.community.domain.Board;
import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    BoardService boardService;
    @Autowired
    CommentRepository commentRepository;

    private Member member;
    private Post post;
    private Board board;


    @BeforeEach
    void initMember() {
        member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        board = boardService.makeBoard(Board.builder().name("test").active(true).build());
        post = postService.writePost(Post.builder().member(member).title("post").board(board).isOpen(false).build());
    }


    @Test
    @DisplayName("댓글 생성 확인")
    void writeComment() {
        //given
        Comment test = Comment.builder().text("TEST").member(member).post(post).build();
        //when
        commentService.writeComment(test);
        //then
        assertThat(test).isEqualTo(commentService.findById(test.getId()));
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

    @Test
    @DisplayName("댓글 수정")
    public void modifyComment() {
        // given
        Comment comment = commentService.writeComment(Comment.builder().post(post).text("comment content").member(member).build());
        Comment modifyComment = Comment.builder().post(post).text("modify comment content").member(member).build();

        // when
        Comment savedComment = commentRepository.findById(comment.getId()).orElseThrow(() -> new IllegalArgumentException("저장되지 않은 코멘트에러"));
        Comment modifiedComment = commentService.modify(savedComment, modifyComment);

        // then
        assertThat(modifyComment.getText()).isEqualTo(modifiedComment.getText());
        assertThat(modifyComment.isOpen()).isEqualTo(modifiedComment.isOpen());
    }
}
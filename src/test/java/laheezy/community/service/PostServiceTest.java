package laheezy.community.service;

import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.dto.post.PostResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class PostServiceTest {
    @Autowired
    BoardService boardService;
    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("보드안에 있는 열린 포스트만 출력하는지 확인한다.")
    public void checkFindAllOpenPostInBoard() throws Exception {
        //given
        Board board = boardService.makeBoard(Board.builder().name("test").active(true).build());
        Member member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        Post post1 = postService.writePost(Post.builder().member(member).title("post").isOpen(true).board(board).build());
        Post post2 = postService.writePost(Post.builder().member(member).title("post").isOpen(false).board(board).build());
        Post post3 = postService.writePost(Post.builder().member(member).title("post").isOpen(true).board(board).build());

        //when
        List<PostResponseDto> test = postService.findAllOpenPostInBoard("test");

        //then
        assertThat(test.size()).isEqualTo(2);
        List<Long> ids = test.stream().map(o -> o.getPostId()).collect(Collectors.toList());
        assertThat(ids).contains(post1.getId(), post3.getId());
        assertThat(ids).doesNotContain(post2.getId());

    }

    @Test
    @DisplayName("포스트의 삭제기능을 확인한다.")
    public void checkDeletePost() throws Exception {
        //given
        Board board = boardService.makeBoard(Board.builder().name("test").active(true).build());
        Member member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        Post post = postService.writePost(Post.builder().member(member).title("post").isOpen(true).board(board).build());

        //when
        postService.deletePost(post);

        //then
        assertThrows(RuntimeException.class, () -> postService.findById(post.getId()));

    }

    @Test
    @DisplayName("포스트의 작성기능을 확인한다.")
    public void checkWritePost() throws Exception {
        //given
        Board board = boardService.makeBoard(Board.builder().name("test").active(true).build());
        Member member = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        Post post = postService.writePost(Post.builder().member(member).title("post").isOpen(true).board(board).build());

        //when
        Post byId = postService.findById(post.getId());

        //then
        Assertions.assertThat(post.getTitle()).isEqualTo(byId.getTitle());

    }
}
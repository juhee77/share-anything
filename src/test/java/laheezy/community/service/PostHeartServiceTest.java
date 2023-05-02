package laheezy.community.service;

import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.PostHeart;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.repository.PostHeartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostHeartServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    BoardService boardService;
    @Autowired
    PostHeartService postHeartService;
    @Autowired
    PostHeartRepository postHeartRepository;
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
    public void addHeart() {
        //given
        //initmember

        //when
        PostHeart postHeart = postHeartService.addHeart(member, post);

        //then
        assertTrue(postHeartService.checkAlreadyHeart(member, post));
        assertThat(postHeartRepository.findById(postHeart.getId()).get().getPost().getId()).isEqualTo(post.getId());
        assertThat(postHeartRepository.findById(postHeart.getId()).get().getMember().getId()).isEqualTo(member.getId());

    }

    @Test
    @Transactional
    void deleteHeart() {
        //given
        PostHeart postHeart = postHeartService.addHeart(member, post);
        assertTrue(postHeartService.checkAlreadyHeart(member, post));

        //when
        postHeartService.deleteHeart(member, post);

        //then
        assertFalse(postHeartService.checkAlreadyHeart(member, post));
    }
}
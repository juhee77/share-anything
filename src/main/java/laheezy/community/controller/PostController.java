package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Board;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.post.PostForm;
import laheezy.community.dto.post.PostModifyDto;
import laheezy.community.dto.post.PostModifyRequestForm;
import laheezy.community.dto.post.PostResponseDto;
import laheezy.community.service.BoardService;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Tag(name = "Post Api", description = "올리는 포스트 글과 관련된 API를 담당합니다.")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final BoardService boardService;

    //@PostMapping(value="/api/post-add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/post")
    @Operation(summary = "포스트 생성", description = "포스트 생성")
    public PostResponseDto makePost(@Valid @RequestBody PostForm postForm) {
        log.info("post 생성");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Board board = boardService.getBoardWithActive(postForm.getBoard());
        Post post = Post.builder()
                .text(postForm.getText())
                .title(postForm.getTitle())
                .isOpen(postForm.isOpen())
                .member(nowLogin)
                .board(board)
                .build();

        Post savedPost = postService.writePost(post);

        return new PostResponseDto().toPostResponseDto(savedPost);
    }

    @PutMapping(value = "/post/{postId}")
    @Operation(summary = "포스트 수정", description = "포스트 수정")
    public PostResponseDto modifyPost(@Valid @RequestBody PostModifyRequestForm pRequestForm, @PathVariable Long postId) {
        log.info("post 수정");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Board modifyBoard = boardService.getBoardWithActive(pRequestForm.getBoard());
        Post post = postService.findById(postId);
        PostModifyDto postModifyDto = new PostModifyDto().toPostModifyDto(pRequestForm, modifyBoard);

        Post savedPost = postService.modifyPost(post, postModifyDto);
        return new PostResponseDto().toPostResponseDto(savedPost);
    }

    @DeleteMapping(value = "/post/{postId}")
    @Operation(summary = "포스트 삭제", description = "post id 를 기준으로 포스트를 삭제한다.")
    public void deletePost(@PathVariable Long postId) {
        log.info("post 수정");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Post post = postService.findById(postId);
        postService.deletePost(post);
    }

    @GetMapping(value = "/my/post")
    @Operation(summary = "본인의 작성 포스트 확인", description = "자신의 포스트 확인")//페이징 기능 넣어야 한다.
    public List<PostResponseDto> findMyPost() {
        log.info("자신의 포스트 확인");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        List<Post> myPost = memberService.getMyPost(nowLogin);
        return getResponseDtos(myPost);
    }

    @GetMapping(value = "/post/{postId}")
    @Operation(summary = "해당 포스트 자세히 보기", description = "postID포스트 확인")//페이징 기능 넣어야 한다.
    public PostResponseDto findEachPost(@PathVariable("postId") Long postId) {
        log.info("해당 포스트 자세히 확인 : {}", postId);
        Post post = postService.findById(postId);
        return new PostResponseDto().toPostResponseDto(post);
    }

    @GetMapping(value = "/follow/post")
    @Operation(summary = "내가 팔로우하는 사람들의 게시글만 본다.")//페이징 기능 넣어야 한다.
    public List<PostResponseDto> findFollowPost() {
        log.info("follow post확인");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        List<Post> followPost = postService.findFollowPost(nowLogin);

        return getResponseDtos(followPost);
    }

    @GetMapping(value = "/post")
    @Operation(summary = "있는 포스트를 전부 가져온다")//페이징 기능 넣어야 한다.
    public List<PostResponseDto> findAllPost() {
        log.info("모든 post확인");
        return postService.findAllPostWithHeartCnt();
    }

    @GetMapping("/board/{boardName}")
    public List<PostResponseDto> findAllPost(@PathVariable("boardName") String boardName) {
        log.info("[BoardController] 해단 board의 post 조회 (공개 되어 있는것만) ");
        boardService.getBoardWithActive(boardName); //board가 활성화 되어있는 board 인지, 사용중지 된 board가 아닌지
        return postService.findAllOpenPostInBoard(boardName);
    }

    private List<PostResponseDto> getResponseDtos(List<Post> myPost) {
        return myPost.stream().map(new PostResponseDto()::toPostResponseDto).collect(Collectors.toList());
    }

}

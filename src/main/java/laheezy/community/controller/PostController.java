package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.post.PostForm;
import laheezy.community.dto.post.PostResponseDto;
import laheezy.community.exception.Fail;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/post")
@Tag(name = "Post Api", description = "올리는 포스트 글과 관련된 API를 담당합니다.")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    //@PostMapping(value="/api/post-add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/add")
    @Operation(summary = "포스트 생성", description = "포스트 생성")
    public PostResponseDto makePost(@Valid @RequestBody PostForm postForm) {
        log.info("post 생성");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Post post = Post.builder()
                .text(postForm.getText())
                .title(postForm.getTitle())
                .isOpen(postForm.isOpen())
                .member(nowLogin)
                .build();

        Post savedPost = postService.writePost(post);

        return PostResponseDto.builder()
                .postId(savedPost.getId())
                .writer(savedPost.getMember().getNickname())
                .title(savedPost.getTitle())
                .text(savedPost.getText())
                .text(savedPost.getText())
                .writeDate(savedPost.getWriteDate())
                .isOpen(savedPost.isOpen()).build();
    }

    @GetMapping(value = "/my")
    @Operation(summary = "본인의 작성 포스트 확인", description = "자신의 포스트 확인")//페이징 기능 넣어야 한다.
    public List<PostResponseDto> findMyPost() {
        log.info("자신의 포스트 확인");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        List<Post> myPost = memberService.getMyPost(nowLogin);
        return getResponseDtos(myPost);
    }

    @GetMapping(value = "/get/{postId}")
    @Operation(summary = "해당 포스트 자세히 보기", description = "postID포스트 확인")//페이징 기능 넣어야 한다.
    public PostResponseDto findEachPost(@PathVariable("postId") Long postId) {
        log.info("해당 포스트 자세히 확인 : {}", postId);
        Post post = postService.findById(postId);
        return PostResponseDto.builder()
                .postId(post.getId())
                .writer(post.getMember().getNickname())
                .title(post.getTitle())
                .text(post.getText())
                .text(post.getText())
                .writeDate(post.getWriteDate())
                .isOpen(post.isOpen()).build();
    }

    @GetMapping(value = "/follow")
    @Operation(summary = "내가 팔로우하는 사람들의 게시글만 본다.")//페이징 기능 넣어야 한다.
    public List<PostResponseDto> findFollowPost() {
        log.info("follow post확인");

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        List<Post> followPost = postService.findFollowPost(nowLogin);

        return getResponseDtos(followPost);
    }

    @GetMapping(value = "/all")
    @Operation(summary = "있는 포스트를 전부 가져온다")
    public List<PostResponseDto> findAllPost() {
        log.info("모든 post확인");
        return postService.findAllPostWithHeartCnt();
        // return getResponseDtos(postService.findAll());
    }

    private List<PostResponseDto> getResponseDtos(List<Post> myPost) {
        return myPost.stream().map(o -> new PostResponseDto(o.getId(), o.getMember().getNickname(), o.getTitle(), o.getText(), o.isOpen(), o.getView(), o.getWriteDate(), o.getPostHearts().size())).collect(Collectors.toList());
    }

}

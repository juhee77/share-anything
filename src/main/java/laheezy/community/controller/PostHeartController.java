package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.PostHeart;
import laheezy.community.exception.Fail;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostHeartService;
import laheezy.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/postheart")
@Tag(name = "postHeart controller", description = "post 좋아요 기능을 관리합니다")
@Slf4j
@RequiredArgsConstructor
public class PostHeartController {
    private final PostService postService;
    private final MemberService memberService;
    private final PostHeartService postHeartService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    //@PostMapping(value="/api/post-add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/like/{postId}")
    @Operation(summary = "포스트 좋아요", description = "포스트 좋아요")
    public PostHeart addPostHeart(@PathVariable("postId") Long postId) {
        log.info("postId {}", postId);
        Post heartPost = postService.findById(postId);
        log.info("hearPost : {}", heartPost);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        log.info("{}", nowLogin);

        PostHeart postHeart = postHeartService.addHeart(nowLogin, heartPost);

        return postHeart;
    }

    @PostMapping(value = "/dislike/{postId}")
    @Operation(summary = "포스트 좋아요 삭제", description = "포스트 좋아요 삭제")
    public void deletePostHeart(@PathVariable("postId") Long postId) {
        Post heartPost = postService.findById(postId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        postHeartService.deleteHeart(nowLogin, heartPost);
    }

    @PostMapping(value = "/checkHeart/{postId}")
    @Operation(summary = "포스트 좋아요 확인", description = "포스트 좋아요 확인")
    public boolean checkPostHeart(@PathVariable("postId") Long postId) {
        Post heartPost = postService.findById(postId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        return postHeartService.checkAlreadyHeart(nowLogin, heartPost);
    }
}

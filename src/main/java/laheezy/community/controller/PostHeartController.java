package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.postheart.PostHeartResponseDto;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostHeartService;
import laheezy.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static laheezy.community.dto.postheart.PostHeartResponseDto.toConvertPostResponseDto;

@RestController
@RequestMapping()
@Tag(name = "postHeart controller", description = "post 좋아요 기능을 관리합니다")
@Slf4j
@RequiredArgsConstructor
public class PostHeartController {
    private final PostService postService;
    private final MemberService memberService;
    private final PostHeartService postHeartService;

    @PostMapping(value = "/post/{postId}/like")
    @Operation(summary = "포스트 좋아요", description = "포스트 좋아요")
    public PostHeartResponseDto addPostHeart(@PathVariable("postId") Long postId) {
        Post heartPost = postService.findById(postId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        log.info("{}", nowLogin);

        return toConvertPostResponseDto(postHeartService.addHeart(nowLogin, heartPost));
    }

    @DeleteMapping(value = "/post/{postId}/like")
    @Operation(summary = "포스트 좋아요 삭제", description = "포스트 좋아요 삭제")
    public void deletePostHeart(@PathVariable("postId") Long postId) {
        Post heartPost = postService.findById(postId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        postHeartService.deleteHeart(nowLogin, heartPost);
    }

    @GetMapping(value = "/post/{postId}/like")
    @Operation(summary = "포스트 좋아요 확인", description = "포스트 좋아요 확인")
    public Boolean checkPostHeart(@PathVariable("postId") Long postId) {
        Post heartPost = postService.findById(postId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        return postHeartService.checkAlreadyHeart(nowLogin, heartPost);
    }
}

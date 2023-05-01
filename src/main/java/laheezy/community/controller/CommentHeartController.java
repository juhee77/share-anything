package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.domain.Comment;
import laheezy.community.domain.CommentHeart;
import laheezy.community.service.MemberService;
import laheezy.community.service.CommentHeartService;
import laheezy.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Tag(name = "CommentHeart controller", description = "Comment 좋아요 기능을 관리합니다")
@Slf4j
@RequiredArgsConstructor
public class CommentHeartController {
    private final CommentService commentService;
    private final MemberService memberService;
    private final CommentHeartService commentHeartService;

    @PostMapping(value = "/like/{commentId}")
    @Operation(summary = "댓글 좋아요", description = "댓글 좋아요")
    public CommentHeart addCommentHeart(@PathVariable("commentId") Long commentId) {
        Comment heartComment = commentService.findById(commentId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        log.info("{}", nowLogin);

        return commentHeartService.addHeart(nowLogin, heartComment);
    }

    @PostMapping(value = "/dislike/{commentId}")
    @Operation(summary = "댓글 좋아요 삭제", description = "댓글 좋아요 삭제")
    public void deleteCommentHeart(@PathVariable("commentId") Long commentId) {
        Comment heartComment = commentService.findById(commentId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        commentHeartService.deleteHeart(nowLogin, heartComment);
    }

    @PostMapping(value = "/checkHeart/{commentId}")
    @Operation(summary = "댓글 좋아요 확인", description = "댓글 좋아요 확인")
    public boolean checkCommentHeart(@PathVariable("commentId") Long commentId) {
        Comment heartComment = commentService.findById(commentId);
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        return commentHeartService.checkAlreadyHeart(nowLogin, heartComment);
    }
}

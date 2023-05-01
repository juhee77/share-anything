package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.comment.CommentRequestDto;
import laheezy.community.dto.comment.CommentResponseDto;
import laheezy.community.service.CommentService;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Tag(name = "CommentController", description = "댓글 API Document")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("/post/{postId}/comment")
    @Operation(summary = "댓글 생성")
    public CommentResponseDto makeComment(@PathVariable("postId") Long postId, @Valid @RequestBody CommentRequestDto requestMakeCommentDto) {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Post post = postService.findById(postId);
        Comment comment = Comment.builder()
                .member(nowLogin)
                .text(requestMakeCommentDto.getText())
                .post(post)
                .isOpen(requestMakeCommentDto.isOpen())
                .build();

        Comment savedCost = commentService.writeComment(comment);
        return new CommentResponseDto().toCommentResponseDto(savedCost);
    }

    @GetMapping("/my/comment")
    @Operation(summary = "본인의 작성 댓글 확인", description = "자신의 댓글 확인")//페이징 기능 넣어야 한다.
    public List<CommentResponseDto> findMyComment() {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        List<Comment> myComments = memberService.getMyComment(nowLogin);
        return changeResponseCommentDtos(myComments);
    }


    @GetMapping("/post/{postId}/comment")
    @Operation(summary = "포스트별 댓글을 확인한다.")
    public List<CommentResponseDto> findPostComment(@PathVariable("postId") Long postId) {
        Post post = postService.findById(postId);
        List<Comment> postComments = post.getComments();
        return changeResponseCommentDtos(postComments);
    }

    @DeleteMapping("/comment/{commentId}")
    @Operation(summary = "댓글을 삭제 한다.")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.removeComment(commentId);
    }


    private List<CommentResponseDto> changeResponseCommentDtos(List<Comment> comments) {
        return comments.stream().map(new CommentResponseDto()::toCommentResponseDto).collect(Collectors.toList());
    }

}

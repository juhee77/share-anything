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
@RequestMapping(value = "/comment")
@Tag(name = "CommentController", description = "댓글 API Document")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("/add")
    @Operation(summary = "댓글 생성")
    public CommentResponseDto makeComment(@Valid @RequestBody CommentRequestDto requestMakeCommentDto) {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Post post = postService.findById(requestMakeCommentDto.getPostId());
        Comment comment = Comment.builder()
                .member(nowLogin)
                .text(requestMakeCommentDto.getText())
                .post(post)
                .isOpen(requestMakeCommentDto.isOpen())
                .build();

        Comment savedCost = commentService.writeComment(comment);
        return CommentResponseDto.toCommentResponseDto(savedCost);
    }

    @GetMapping("/my")
    @Operation(summary = "본인의 작성 댓글 확인", description = "자신의 댓글 확인")//페이징 기능 넣어야 한다.
    public List<CommentResponseDto> makePost() {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        List<Comment> myComments = memberService.getMyComment(nowLogin);
        return changeResponseCommentDtos(myComments);
    }


    private List<CommentResponseDto> changeResponseCommentDtos(List<Comment> comments) {
        return comments.stream().map(CommentResponseDto::toCommentResponseDto).collect(Collectors.toList());
    }

}

package laheezy.community.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.RequestMakeCommentDto;
import laheezy.community.exception.Fail;
import laheezy.community.service.CommentService;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/comment")
@Tag(name = "CommentController", description = "댓글 API Document")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @PostMapping("/add")
    public CommentResponseDto makeComment(@Valid @RequestBody RequestMakeCommentDto requestMakeCommentDto) {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Post post = postService.findById(requestMakeCommentDto.getPostId());
        Comment comment = Comment.builder()
                .member(nowLogin)
                .text(requestMakeCommentDto.getText())
                .post(post)
                .isOpen(requestMakeCommentDto.isOpen())
                .build();
        Comment savedCost = commentService.writeComment(comment);
        return new CommentResponseDto(savedCost.getMember().getLoginId(), savedCost.getPost().getId(), savedCost.getText(), savedCost.isOpen());
    }

    @Data
    @AllArgsConstructor
    private static class CommentResponseDto {
        private String writerNickname; //게시글 작성자
        private long postId; //포스트 id
        private String text;
        private boolean open;
    }
}

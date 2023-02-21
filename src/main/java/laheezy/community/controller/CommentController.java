package laheezy.community.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Comment;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.dto.RequestMakeCommentDto;
import laheezy.community.service.CommentService;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
@Tag(name = "CommentController", description = "댓글 API Document")
@Slf4j
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final MemberService memberService;

    @PostMapping("/api/comment-add")
    public CommentResponseDto makeComment(@Valid @RequestBody RequestMakeCommentDto requestMakeCommentDto) {
        Member author = memberService.findByNickname(requestMakeCommentDto.getNickname());
        Post post = postService.findById(requestMakeCommentDto.getPostId());
        Comment comment = Comment.builder()
                .member(author)
                .text(requestMakeCommentDto.getText())
                .post(post)
                .isOpen(requestMakeCommentDto.isOpen())
                .build();
        Comment savedCost = commentService.writeComment(comment);
        return new CommentResponseDto(savedCost.getMember().getNickname(), savedCost.getPost().getId(), savedCost.getText(), savedCost.isOpen());
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

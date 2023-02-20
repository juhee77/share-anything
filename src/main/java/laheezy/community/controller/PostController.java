package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.form.PostForm;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/")
@Tag(name = "Template", description = "템플릿 API Document")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    //@PostMapping(value="/api/post-add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/api/post-add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "포스트 생성", description = "포스트 생성")
    public PostResponseDto makePost(@Valid @ModelAttribute PostForm postForm) { //TODO: 리턴값 DTO로 수정 해야함(무한 루프 돌것임)
        log.info("post={}", postForm);

        Member author = memberService.findByNickname(postForm.getWriterNickname());
        log.info("user={}",author);
        Post post = Post.builder()
                .member(author)
                .text(postForm.getText())
                .title(postForm.getTitle())
                .isOpen(postForm.isOpen())
                .build();

        Post savedPost = postService.writePost(post);

        return new PostResponseDto(author.getNickname(),savedPost.getTitle(), savedPost.getText(),savedPost.isOpen());
    }

    @Data
    @AllArgsConstructor
    private static class PostResponseDto {
        private String writerNickname; //게시글 작성자
        private String title;
        private String text;
        private boolean open;
    }

}

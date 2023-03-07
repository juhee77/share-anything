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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/post")
@Tag(name = "Post Api", description = "올리는 포스트 글과 관련된 API를 담당합니다.")
@Slf4j
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    //@PostMapping(value="/api/post-add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "포스트 생성", description = "포스트 생성")
    public PostResponseDto makePost(@Valid @ModelAttribute PostForm postForm) {


        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Post post = Post.builder()
                .member(nowLogin)
                .text(postForm.getText())
                .title(postForm.getTitle())
                .isOpen(postForm.isOpen())
                .build();

        Post savedPost = postService.writePost(post);

        return new PostResponseDto(nowLogin.getNickname(), savedPost.getTitle(), savedPost.getText(), savedPost.isOpen());
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

package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.exception.Fail;
import laheezy.community.form.PostForm;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/post")
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
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "포스트 생성", description = "포스트 생성")
    public PostResponseDto makePost(@Valid @ModelAttribute PostForm postForm) {

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Post post = Post.builder()
                .text(postForm.getText())
                .title(postForm.getTitle())
                .isOpen(postForm.isOpen())
                .build();
        post.setMember(nowLogin);

        Post savedPost = postService.writePost(post);

        return new PostResponseDto(nowLogin.getNickname(), savedPost.getTitle(), savedPost.getText(), savedPost.isOpen());
    }


    //이럴때는 포스트야 겟이야..?
    @GetMapping(value = "/get-mypost")
    @Operation(summary = "본인의 작성 포스트 확인", description = "자신의 포스트 확인")//페이징 기능 넣어야 한다.
    public List<PostViwResponseDto> makePost() {

        Member nowLogin = memberService.getMemberWithAuthorities().get();
        List<Post> myPost = memberService.getMyPost(nowLogin);
        List<PostViwResponseDto> responseDtos = myPost.stream().map(o -> new PostViwResponseDto(o.getId(), o.getTitle(), o.getText(), o.isOpen(), o.getView(), o.getWriteDate())).collect(Collectors.toList());

        return responseDtos;
    }

    //이럴때는 포스트야 겟이야..?
    @GetMapping(value = "/get/{postId}")
    @Operation(summary = "해당 포스트 자세히 보기", description = "postID포스트 확인")//페이징 기능 넣어야 한다.
    public PostViwResponseDto makePost(@PathVariable("postId") Long postId) {
        Post post = postService.findById(postId);
        PostViwResponseDto responseDto = new PostViwResponseDto(post.getId(), post.getTitle(), post.getText(), post.isOpen(), post.getView(), post.getWriteDate());

        return responseDto;
    }

    @Data
    @AllArgsConstructor
    private static class PostResponseDto {
        private String writerNickname; //게시글 작성자
        private String title;
        private String text;
        private boolean open;
    }

    @Data
    @AllArgsConstructor
    private static class PostViwResponseDto {
        private Long postId;
        private String title; //제목
        private String text; //내용
        private boolean isOpen; //공개 비공개
        private long view; // 조회수
        private LocalDateTime writeDate; //작성 날짜
    }

}

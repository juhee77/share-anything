package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import laheezy.community.domain.Member;
import laheezy.community.domain.Post;
import laheezy.community.domain.file.File;
import laheezy.community.domain.file.FileType;
import laheezy.community.service.FileService;
import laheezy.community.service.MemberService;
import laheezy.community.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@Slf4j
public class FileController {
    private final MemberService memberService;
    private final FileService fileService;
    private final PostService postService;

    @RequestMapping(value = "/profile/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "프로필 이미지 업로드")
    public ResponseEntity<UrlResource> imageUpload(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        nowLogin = fileService.storeProFile(multipartFile, nowLogin);
        File uploadFile = nowLogin.getProfileImage();

        String uploadFileName = uploadFile.getOriginName();
        UrlResource resource =  fileService.convertToUrlResource(uploadFile);
        log.info("uploadFileName={}", uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);

        //return resource;//이진 파일로 나간다.
    }

    @RequestMapping(value = "/postfile/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "포스트 이미지 업로드")
    public ResponseEntity<UrlResource> postFileUpload(@RequestPart("multipartFile") MultipartFile multipartFile, @RequestParam("caption") String caption, @RequestParam("postId") Long id) throws IOException {
        Post post = postService.findById(id);
        File uploadFile = fileService.storePostFile(multipartFile, caption, post, FileType.POST);

        String uploadFileName = uploadFile.getOriginName();
        UrlResource resource =  fileService.convertToUrlResource(uploadFile);
        log.info("uploadFileName={}", uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);

    }
}

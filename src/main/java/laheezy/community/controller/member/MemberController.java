package laheezy.community.controller.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.exception.Fail;
import laheezy.community.service.FileService;
import laheezy.community.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping
@Tag(name = "Member", description = "Member Controller")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final FileService fileService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    //admin권한에서 테스트 용
    @GetMapping("/user/{loginId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MemberResponseDto> getUserInfo(@PathVariable String loginId) {
        Member findMember = memberService.getMemberWithAuthorities(loginId).get();
        return ResponseEntity.ok(convertToResponseDto(findMember));
    }

    @NotNull
    private MemberController.MemberResponseDto convertToResponseDto(Member findMember) {
        return new MemberResponseDto(findMember.getNickname(), findMember.getLoginId(),
                findMember.getEmail(), "file:" + fileService.getFullPath(findMember.getProfileImage().getStoreName()));
    }

    @GetMapping("/me-profile")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        Member findMember = memberService.getMemberWithAuthorities().get();
        return ResponseEntity.ok(convertToResponseDto(findMember));
    }

    @GetMapping("/get-allmember")
    public List<MemberResponseDto> findAll() {
        List<Member> allMember = memberService.findAllMember();
        List<MemberResponseDto> responseDto = new ArrayList<>();
        for (Member member : allMember) {
            responseDto.add(convertToResponseDto(member));
        }
        return responseDto;
    }

    @PostMapping("/member/modify/nickname")
    public ResponseEntity<MemberResponseDto> modifyNickname(@RequestBody Map<String, String> nickname) {
        log.info("modifyName");
        Member findMember = memberService.getMemberWithAuthorities().get();
        Member modified = memberService.modifyNickname(findMember, nickname.get("nickname"));

        return ResponseEntity.ok(convertToResponseDto(modified));
    }

    @PostMapping("/member/modify/password")
    public void modifyPassword(@RequestBody Map<String, String> password) throws JsonProcessingException {
        log.info("modifyPassword");
        Member findMember = memberService.getMemberWithAuthorities().get();
        //1. 비밀번호 일치 확인
        memberService.checkPassword(findMember, password.get("exPassword"));

        //2. 비밀번호 변경
        memberService.modifyPassword(findMember, password.get("newPassword"));
    }

    @Data
    @ToString
    @AllArgsConstructor
    private static class MemberResponseDto {
        private String nickname;
        private String loginId;
        private String email;
        private String profileImageUrl;
    }
}

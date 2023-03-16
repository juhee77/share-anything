package laheezy.community.controller.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.exception.Fail;
import laheezy.community.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());

    }


    @GetMapping("/")
    @Operation(summary = "메인 화면 이동", description = "메인화면 출력", tags = {"View"})
    public String selectTemplateView() {
        return "/";
    }

    //admin권한에서 테스트 용
    @GetMapping("/user/{loginId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<MemberResponseDto> getUserInfo(@PathVariable String loginId) {
        Member findMember = memberService.getMemberWithAuthorities(loginId).get();
        return ResponseEntity.ok(new MemberResponseDto(findMember.getNickname(), findMember.getLoginId(), findMember.getEmail()));
    }

    @GetMapping("/me-profile")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        Member findMember = memberService.getMemberWithAuthorities().get();
        return ResponseEntity.ok(new MemberResponseDto(findMember.getNickname(), findMember.getLoginId(), findMember.getEmail()));
    }

    @GetMapping("/get-allmember")
    public List<MemberResponseDto> findAll() {
        List<Member> allMember = memberService.findAllMember();
        List<MemberResponseDto> responseDto = new ArrayList<>();
        for (Member member : allMember) {
            responseDto.add(new MemberResponseDto(member.getNickname(), member.getLoginId(), member.getEmail()));
        }
        return responseDto;
    }

    @PostMapping("/member/modify/nickname")
    public ResponseEntity<MemberResponseDto> modifyNickname(@RequestBody Map<String, String> nickname) throws JsonProcessingException {
        log.info("modifyName");
        Member findMember = memberService.getMemberWithAuthorities().get();
        Member modified = memberService.modifyNickname(findMember, nickname.get("nickname"));

        return ResponseEntity.ok(new MemberResponseDto(modified.getNickname(), modified.getLoginId(), modified.getEmail()));
    }

    @PostMapping("/member/modify/password")
    public void modifyPassword(@RequestBody Map<String, String> password) throws JsonProcessingException {
        log.info("modifyPassword");
        Member findMember = memberService.getMemberWithAuthorities().get();
        //1. 비밀번호 일치 확인
        memberService.checkPassword(findMember,password.get("exPassword"));

        //2. 비밀번호 변경
        memberService.modifyPassword(findMember,password.get("newPassword"));
    }

    @Data
    @ToString
    @AllArgsConstructor
    private static class MemberResponseDto {
        private String nickname;
        private String loginId;
        private String email;
    }
}

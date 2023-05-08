package laheezy.community.controller.member;

import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.service.FileService;
import laheezy.community.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    //admin권한에서 테스트 용
    @GetMapping("/member/{loginId}")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')") --> spring security config 부분에서 처리
    public ResponseEntity<MemberResponseDto> getUserInfo(@PathVariable String loginId) {
        Member findMember = memberService.getMemberWithAuthorities(loginId).get();
        return ResponseEntity.ok(convertToResponseDto(findMember));
    }

    @GetMapping("/member")
    public List<MemberResponseDto> findAll() {
        List<Member> allMember = memberService.findAllMember();
        List<MemberResponseDto> responseDto = new ArrayList<>();
        for (Member member : allMember) {
            responseDto.add(convertToResponseDto(member));
        }
        return responseDto;
    }

    @GetMapping("/my/profile")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        Member findMember = memberService.getMemberWithAuthorities().get();
        return ResponseEntity.ok(convertToResponseDto(findMember));
    }


    @PatchMapping("/member/{loginId}/nickname")
    public ResponseEntity<MemberResponseDto> modifyNickname(@PathVariable String loginId, @RequestBody Map<String, String> nickname) {
        log.info("modifyName");
        Member findMember = memberService.getMemberWithAuthorities().get();
        Member modified = memberService.modifyNickname(findMember, nickname.get("nickname"));

        return ResponseEntity.ok(convertToResponseDto(modified));
    }

    @PatchMapping("/member/{loginId}/password")
    public void modifyPassword(@PathVariable String loginId, @RequestBody Map<String, String> password) {
        log.info("modifyPassword");
        Member findMember = memberService.getMemberWithAuthorities().get();
        //TODO :: loginID와 현재 리프레시 토큰의 인증 정보를 비교(nick name도)

        //1. 비밀번호 일치 확인
        memberService.checkPassword(findMember, password.get("exPassword"));

        //2. 비밀번호 변경
        memberService.modifyPassword(findMember, password.get("newPassword"));
    }

    @PostMapping("/member/{loginId}/logout")
    public void logoutUser(@PathVariable String loginId) {
        Member findMember = memberService.getMemberWithAuthorities(loginId).get();
        memberService.logout(findMember);
    }

    @DeleteMapping("/member/{loginId}")
    public void DeleteUser(@PathVariable String loginId) {
        //Member member = memberService.getMemberWithAuthorities().get();
        Member member = memberService.getMemberWithAuthorities(loginId).get();
        //TODO 같은 사람인지 확인 (더블 체크 필요)
        memberService.deleteMember(member);
    }

    private MemberController.MemberResponseDto convertToResponseDto(Member findMember) {
        if (findMember.getProfileImage() != null)
            return new MemberResponseDto(findMember.getNickname(), findMember.getLoginId(),
                    findMember.getEmail(), "file:" + fileService.getFullPath(findMember.getProfileImage().getStoreName()));
        //이미지가 없는 경우
        return new MemberResponseDto(findMember.getNickname(), findMember.getLoginId(),
                findMember.getEmail(), null);
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

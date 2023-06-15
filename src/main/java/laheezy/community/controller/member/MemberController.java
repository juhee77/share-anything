package laheezy.community.controller.member;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.dto.member.MemberResponseDto;
import laheezy.community.service.FileService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static laheezy.community.dto.member.MemberResponseDto.getInstance;


@RestController
@RequestMapping
@Tag(name = "Member", description = "Member Controller")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final FileService fileService;

    //admin
    @GetMapping("/member/{loginId}")
    @Operation(summary = "loginId 조회(한명의 유저 확인)", tags = {"admin"})
    public ResponseEntity<MemberResponseDto> getUserInfo(@PathVariable String loginId) {
        //TODO 로그인 아이디와 path의 로그인 아이디 확인 방법
        return ResponseEntity.ok(getInstance(memberService.getMemberWithAuthorities(loginId).orElseThrow(RuntimeException::new)));
    }

    @GetMapping("/member")
    @Operation(summary = "전체멤버 확인")
    public ResponseEntity<List<MemberResponseDto>> findAll() {
        return ResponseEntity.ok(memberService.findAllMember());
    }

    @GetMapping("/my/profile")
    @Operation(description = "profile 확인", tags = {"my"})
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        return ResponseEntity.ok(getInstance(getNowContextMember()));
    }


    @PatchMapping("/member/{loginId}/nickname")
    @Operation(summary = "전체멤버 확인")
    public ResponseEntity<MemberResponseDto> modifyNickname(@PathVariable String loginId, @RequestBody Map<String, String> nickname) {
        return ResponseEntity.ok(getInstance(memberService.modifyNickname(getNowContextMember(), nickname.get("nickname"))));
    }

    //현재 로그인한 유저정보
    private Member getNowContextMember() {
        return memberService.getMemberWithAuthorities().orElseThrow(RuntimeException::new);
    }

    @PatchMapping("/member/{loginId}/password")
    @Operation(summary = "패스워드 변경", tags = {"my"})
    public void modifyPassword(@PathVariable String loginId, @RequestBody Map<String, String> password) {
        //TODO :: loginID와 현재 리프레시 토큰의 인증 정보를 비교(nick name도)
        memberService.updatePassword(memberService.getMemberWithAuthorities().get(), password);
    }

    @PostMapping("/member/{loginId}/logout")
    @Operation(summary = "log out", tags = {"my"})
    public void logoutUser(@PathVariable String loginId) {
        memberService.logout(getNowContextMember());
    }

    @DeleteMapping("/member/{loginId}")
    @Operation(summary = "회원 탈퇴", tags = {"my"})
    public void DeleteUser(@PathVariable String loginId) {
        //TODO 같은 사람인지 확인 (더블 체크 필요)
        memberService.deleteMember(getNowContextMember());
    }

}

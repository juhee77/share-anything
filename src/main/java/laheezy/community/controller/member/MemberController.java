package laheezy.community.controller.member;

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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
    @GetMapping("/user/{nickname}")
    //  @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MemberResponseDto> getUserInfo(@PathVariable String nickname) {
        Member findMember = memberService.getMemberWithAuthorities(nickname).get();
        return ResponseEntity.ok(new MemberResponseDto(findMember.getName(), findMember.getNickname(), findMember.getEmail()));
    }

    @GetMapping("/me-profile")
    // @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        Member findMember = memberService.getMemberWithAuthorities().get();
        return ResponseEntity.ok(new MemberResponseDto(findMember.getName(), findMember.getNickname(), findMember.getEmail()));
    }

    @GetMapping("/get-allmember")
    public List<MemberResponseDto> findAll() {
        List<Member> allMember = memberService.findAllMember();
        List<MemberResponseDto> responseDto = new ArrayList<>();
        for (Member member : allMember) {
            responseDto.add(new MemberResponseDto(member.getName(), member.getNickname(), member.getEmail()));
        }
        return responseDto;
    }

    @Data
    @ToString
    @AllArgsConstructor
    private static class MemberResponseDto {
        private String name;
        private String nickname;
        private String email;
    }
}

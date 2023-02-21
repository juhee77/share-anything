package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Member;
import laheezy.community.dto.RequestMakeMemberDto;
import laheezy.community.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
//https://covenant.tistory.com/279


@RestController
@RequestMapping(value = "/")
@Tag(name = "Template", description = "템플릿 API Document")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/")
    @Operation(summary = "메인 화면 이동", description = "메인화면 출력", tags = {"View"})
    public String selectTemplateView() {
        return "/";
    }

    @PostMapping("/api/user-add")
    @Operation(summary = "유저 생성", description = "유저 생성후 userID 반환.")
    public MemberResponseDto makeUser(@Valid @RequestBody RequestMakeMemberDto userMakeDto) {
        log.info("userRequestInfo={}", userMakeDto);
        Member member = Member.builder()
                .email(userMakeDto.getEmail())
                .name(userMakeDto.getName())
                .loginId(userMakeDto.getLoginId())
                .password(userMakeDto.getPassword())
                .nickname(userMakeDto.getNickname()).build();

        Member savedMember = memberService.join(member);
        return new MemberResponseDto(savedMember.getName(), savedMember.getNickname(), savedMember.getLoginId(), savedMember.getPassword(), savedMember.getEmail());
    }

    @Data
    @AllArgsConstructor
    private static class MemberResponseDto {
        private String name;
        private String nickname;
        private String loginId;
        private String password;
        private String email;
    }

}

package laheezy.community.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import laheezy.community.domain.Member;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.jwt.TokenRequestDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.dto.member.MemberResponseDto;
import laheezy.community.jwt.TokenProvider;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Member Auth", description = "Member Auth Controller")
@Slf4j
public class MemberAuthController {
    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원 가입 API")
    @ApiResponse(responseCode = "200", description = "successfulOperation", content = @Content(schema = @Schema(implementation = MemberRequestDto.class)))
    //@ApiResponse(responseCode = "400", description = "failOperation", content = @Content(schema = @Schema(implementation = MemberResponseDto.class)))
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        Member savedMember = memberService.signup(memberRequestDto);
        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .nickname(savedMember.getNickname())
                .joinDateTime(savedMember.getJoinDate())
                .build();

        return ResponseEntity.ok(memberResponseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        TokenDto loginResponse = memberService.login(loginDto);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재 발급")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }

}

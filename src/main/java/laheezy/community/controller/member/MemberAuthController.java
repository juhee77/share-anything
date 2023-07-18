package laheezy.community.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import laheezy.community.domain.Member;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.jwt.TokenRequestDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.dto.member.MemberResponseDto;
import laheezy.community.service.FileService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Member Auth", description = "Member Auth Controller")
@Slf4j
public class MemberAuthController {
    private final MemberService memberService;

    @PostMapping(value = "/signup",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입", description = "회원 가입 API")
    @ApiResponse(responseCode = "200", description = "successfulOperation", content = @Content(schema = @Schema(implementation = MemberRequestDto.class)))
    public ResponseEntity<MemberResponseDto> signup(@ModelAttribute @Valid MemberRequestDto memberRequestDto) throws MalformedURLException {
        Member savedMember = memberService.signup(memberRequestDto);
        return ResponseEntity.ok(MemberResponseDto.getInstance(savedMember));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(memberService.login(loginDto));
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재 발급")
    public ResponseEntity<TokenDto> reissue(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }

}

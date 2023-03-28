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
import laheezy.community.exception.Fail;
import laheezy.community.service.FileService;
import laheezy.community.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Member Auth", description = "Member Auth Controller")
@Slf4j
public class MemberAuthController {
    private final FileService fileService;
    private final MemberService memberService;


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @PostMapping(value = "/signup")
//, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "회원 가입 API")
    @ApiResponse(responseCode = "200", description = "successfulOperation", content = @Content(schema = @Schema(implementation = MemberRequestDto.class)))
    //@ApiResponse(responseCode = "400", description = "failOperation", content = @Content(schema = @Schema(implementation = MemberResponseDto.class)))
    public ResponseEntity<MemberResponseDto> signup(@ModelAttribute @Valid MemberRequestDto memberRequestDto) throws IOException {
        log.info("회원가입 시도:{} ", memberRequestDto);
        //회원 가입부분(이미지 업로드 전)
        Member savedMember = memberService.signup(memberRequestDto);
        //회원 이미지 업로드 부분
        fileService.storeProFile(memberRequestDto.getProfileImg(), savedMember);

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .email(savedMember.getEmail())
                .loginId(savedMember.getLoginId())
                .nickname(savedMember.getNickname())
                .joinDateTime(savedMember.getJoinDate())
                //.profileImage(new UrlResource("file:" + fileService.getFullPath(savedMember.getProfileImage().getStoreName())))
                .build();
        log.info("회원가입 완료");
        return ResponseEntity.ok(memberResponseDto);
    }


//    @PostMapping("/authenticate")
//    public ResponseEntity<String> authorize(@Valid @RequestBody LoginDto loginDto) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());
//
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = tokenProvider.createToken(authentication);
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
//    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        TokenDto loginResponse = memberService.login(loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재 발급")
    public ResponseEntity<TokenDto> reissue(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(memberService.reissue(tokenRequestDto));
    }

}

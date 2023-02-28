package laheezy.community.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Member;
import laheezy.community.repository.MemberRepository;
import laheezy.community.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/")
@Tag(name = "Member", description = "Member Controller")
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ExceptionHandler({IllegalArgumentException.class,RuntimeException.class})
    public Fail checkLogin(Exception e) {
        return new Fail(e.getMessage());
    }


    @GetMapping("/")
    @Operation(summary = "메인 화면 이동", description = "메인화면 출력", tags = {"View"})
    public String selectTemplateView() {
        return "/";
    }

//    @PostMapping("/api/user-add")
//    @Operation(summary = "유저 생성", description = "유저 생성후 userID 반환.")
//    public MemberResponseDto makeUser(@Valid @RequestBody MemberRequestDto userMakeDto) {
//        log.info("userRequestInfo={}", userMakeDto);
//        Member member = Member.builder()
//                .email(userMakeDto.getEmail())
//                .name(userMakeDto.getName())
//                .loginId(userMakeDto.getLoginId())
//                .password(userMakeDto.getPassword())
//                .nickname(userMakeDto.getNickname()).build();
//
//        Member savedMember = memberService.join(member);
//        return new MemberResponseDto(savedMember.getName(), savedMember.getNickname(), savedMember.getLoginId(), savedMember.getPassword(), savedMember.getEmail());
//    }


    @GetMapping("/get-allmember")
    public List<MemberResponseDto> findAll(){
        List<Member> allMember = memberService.findAllMember();
        List<MemberResponseDto> responseDto = new ArrayList<>();
        for (Member member : allMember) {
            responseDto.add(new MemberResponseDto(member.getName(), member.getNickname(), member.getPassword(), member.getEmail()));
        }
        return responseDto;

    }

    @Data
    @ToString
    @AllArgsConstructor
    private static class MemberResponseDto {
        private String name;
        private String nickname;
        private String password;
        private String email;
    }


    private static class Fail{
        private static final HttpStatus status = HttpStatus.NOT_FOUND;
        private String msg;

        public Fail(String msg) {
            this.msg = msg;
        }
    }

}

package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.domain.Following;
import laheezy.community.domain.Member;
import laheezy.community.exception.Fail;
import laheezy.community.service.FollowingService;
import laheezy.community.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Tag(name = "Following")
@Slf4j
@RequiredArgsConstructor
public class FollowingController {
    private final MemberService memberService;
    private final FollowingService followingService;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public Fail checkLogin(Exception e) {
        return new Fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @PostMapping("/follow/{memberId}")
    @Operation(summary = "memberId를 팔로우 한다.")
    public ResponseEntity<List<MemberResponseShortDto>> addFollwing(@PathVariable("memberId") Long memberId) {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Member following = memberService.findById(memberId);
        checkSamePeople(nowLogin, following);
        followingService.addFollowing(nowLogin, following);

        List<Following> follow = nowLogin.getFollowing();
        List<MemberResponseShortDto> shortDtos = follow.stream().map(o -> new MemberResponseShortDto(o.getMemberB().getLoginId(), o.getMemberB().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(shortDtos);
    }

    private void checkSamePeople(Member follower, Member following) {
        if (follower == following) {
            throw new RequestRejectedException("본인은 불가능합니다");
        }
    }

    @PostMapping("/disfollow/{memberId}")
    public ResponseEntity<String> removeFollowing(@PathVariable("memberId") Long memberId) {
        Member nowLogin = memberService.getMemberWithAuthorities().get();
        Member following = memberService.findById(memberId);
        followingService.deleteFollowing(nowLogin, following);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/checkfollower")
    @Operation(summary = "나를 팔로잉 하는 사람 확인")
    public ResponseEntity<List<MemberResponseShortDto>> getMyFollower() {
        List<Following> follower = memberService.getMemberWithAuthorities().get().getFollower();
        List<MemberResponseShortDto> shortDtos = follower.stream().map(o -> new MemberResponseShortDto(o.getMemberB().getLoginId(), o.getMemberB().getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(shortDtos);
    }

    @GetMapping("/checkfollowing")
    @Operation(summary = "내가 팔로우 하는 사람 확인")
    public ResponseEntity<List<MemberResponseShortDto>> getMyFollowing() {
        List<Following> follow = memberService.getMemberWithAuthorities().get().getFollowing();
        List<MemberResponseShortDto> shortDtos = follow.stream().map(o -> new MemberResponseShortDto(o.getMemberB().getLoginId(), o.getMemberB().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(shortDtos);
    }

    @Data
    @ToString
    @AllArgsConstructor
    private static class MemberResponseShortDto {
        private String nickname;
        private Long userId;
    }

}

package laheezy.community.service;

import laheezy.community.domain.Member;
import laheezy.community.dto.member.MemberRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
class FollowingServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private FollowingService followingService;
    Member memberA, memberB;
    @BeforeEach
    void initMember() {
        memberA = memberService.signup(new MemberRequestDto("pass", "loginId", "nick", "go@go.com"));
        memberB = memberService.signup(new MemberRequestDto("pass2", "loginId2", "nick2", "go2@go.com"));
    }

    @Test
    void addFollowing() {
        //given
        //when
        assertFalse(followingService.checkAlreadyFollowing(memberA, memberB));
        followingService.addFollowing(memberA, memberB);

        //then
        assertTrue(followingService.checkAlreadyFollowing(memberA, memberB));
    }

    @Test
    void deleteFollowing() {
        //given
        followingService.addFollowing(memberA, memberB);
        assertTrue(followingService.checkAlreadyFollowing(memberA, memberB));

        //when
        followingService.deleteFollowing(memberA, memberB);

        //then
        assertFalse(followingService.checkAlreadyFollowing(memberA, memberB));
    }
}
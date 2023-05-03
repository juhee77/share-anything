package laheezy.community.controller;

import laheezy.community.domain.Member;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.dto.member.LoginDto;
import laheezy.community.dto.member.MemberRequestDto;
import laheezy.community.service.FollowingService;
import laheezy.community.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
@ActiveProfiles("test")
class FollowingControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private FollowingService followingService;

    private Member memberA, memberB;
    private TokenDto loginA, loginB;

    @BeforeEach
    void initMember() {
        memberA = memberService.signup(new MemberRequestDto("pass", "name", "nick", "go1@go"));
        memberB = memberService.signup(new MemberRequestDto("pass2", "name2", "nick2", "go2@go"));
        loginA = memberService.login(new LoginDto("name", "pass"));
        loginB = memberService.login(new LoginDto("name2", "pass2"));
    }

    @Test
    void addFollwing() throws Exception {
        //when
        mockMvc.perform(post("/follow/{memberId}", memberB.getId())
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname", memberB.getLoginId()).exists())
                .andExpect(jsonPath("$[0].userId", memberB.getId()).exists());

        //then
        assertTrue(followingService.checkAlreadyFollowing(memberA, memberB));
    }

    @Test
    void removeFollowing() throws Exception {
        //given
        followingService.addFollowing(memberA, memberB);
        assertThat(memberA.getFollowing().size()).isEqualTo(1);

        //when
        mockMvc.perform(delete("/follow/{memberId}", memberB.getId())
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andDo(print());
        //then
        assertFalse(followingService.checkAlreadyFollowing(memberA, memberB));
    }

    @Test
    void getMyFollower() throws Exception {
        //given
        followingService.addFollowing(memberA, memberB);
        //when
        mockMvc.perform(get("/my/follower")
                        .header("Authorization", "Bearer " + loginB.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname", memberA.getLoginId()).exists())
                .andExpect(jsonPath("$[0].userId", memberA.getId()).exists());
        //then
        assertThat(memberB.getFollower().size()).isEqualTo(1);
    }

    @Test
    void getMyFollowing() throws Exception {
        //given
        followingService.addFollowing(memberA, memberB);

        //when
        mockMvc.perform(get("/my/following")
                        .header("Authorization", "Bearer " + loginA.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname", memberB.getLoginId()).exists())
                .andExpect(jsonPath("$[0].userId", memberB.getId()).exists());

        //then
        assertThat(memberA.getFollowing().size()).isEqualTo(1);
    }
}
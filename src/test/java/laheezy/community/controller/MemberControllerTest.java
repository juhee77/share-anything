package laheezy.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import laheezy.community.dto.member.MemberRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
//@WebMvcTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//AutoConfigureMockMvc

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {
    //Mock객체 세팅

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

@Test
    public void 유저객체생성확인() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        MemberRequestDto userMakeDto = new MemberRequestDto("password", "test", "nick", "bo@google.com");
        String requestBody = objectMapper.writeValueAsString(userMakeDto);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("test"))
                .andExpect(jsonPath("nickname").value("nick"))
                .andExpect(jsonPath("email").value("bo@google.com"));

    }

}
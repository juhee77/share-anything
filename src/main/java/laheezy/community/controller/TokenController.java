package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.dto.jwt.TokenDto;
import laheezy.community.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "test", description = "test용")
@Slf4j
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/findToken")
    @Operation(summary = "테스트 토큰을 생성", description = "테스트 토큰 생성")
    public String makeToken() {
        TokenDto tokenDto = tokenService.makeTestToken();
        return tokenDto.getAccessToken();
    }
}

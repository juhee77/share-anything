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
@Tag(name = "Template", description = "템플릿 API Document")
@Slf4j
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/findToken")
    @Operation(summary = "포스트 생성", description = "포스트 생성")
    public ResponseEntity<TokenDto> makeToken() {
        TokenDto tokenDto = tokenService.makeTestToken();
        return ResponseEntity.ok(tokenDto);
    }
}

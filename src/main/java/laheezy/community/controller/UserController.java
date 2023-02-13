package laheezy.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import laheezy.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//https://covenant.tistory.com/279


@RestController
@RequestMapping(value = "/api/v1/temp/")
@Tag(name = "Template", description = "템플릿 API Document")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/")
    @Operation(summary = "메인 화면 이동", description = "메인화면 출력", tags = {"View"})
    public String selectTemplateView() {
        return "/";
    }

    @GetMapping("/api/user/{name}")
    @Operation(summary = "테스트 유저 생성", description = "유저 생성후 userID 반환.")
    public long makeUser(@PathVariable("name") String name) {
        Long userId = userService.saveUser(name);
        return userId;
    }
}

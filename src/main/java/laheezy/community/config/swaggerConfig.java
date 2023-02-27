package laheezy.community.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "채팅서비스 API 명세서",
                description = "헥사고날 아키텍처 기반 채팅 서비스 API 명세서",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class swaggerConfig {
//    // JWT SecurityContext 구성
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return List.of(new SecurityReference("Authorization", authorizationScopes));
//    }
//
//    // ApiKey 정의
//    private ApiKey apiKey() {
//        return new ApiKey("Authorization", "Authorization", "header");
//    }

//    @Bean
//    public GroupedOpenApi chatOpenApi() {
//        String[] paths = {"/v1/**"};
//
//        return GroupedOpenApi.builder()
//                .group("채팅서비스 API v1")
//                .pathsToMatch(paths)
//                .build();
//    }
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearer-key",
//                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
//    }


}

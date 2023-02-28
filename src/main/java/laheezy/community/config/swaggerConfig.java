package laheezy.community.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import laheezy.community.common.AuthConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

//@OpenAPIDefinition(info = @Info(title = "커뮤니티",  description = "커뮤니티 서비스", version = "v1")) //application.properties에 선언해서 bean 형태로 주입도 가능
@RequiredArgsConstructor
@Configuration
public class swaggerConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
        Info info = new Info()
                .title("community API doc").version(appVersion)
                .description("Spring Boot를 이용한 웹 애플리케이션 API입니다.");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");


        SecurityRequirement schemeRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Authorization", securityScheme))
                .security(List.of(schemeRequirement))
                .info(info);
    }
}

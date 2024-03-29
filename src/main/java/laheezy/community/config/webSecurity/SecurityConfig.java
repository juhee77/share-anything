package laheezy.community.config.webSecurity;

import laheezy.community.exception.ExceptionHandleFilter;
import laheezy.community.jwt.JwtAccessDeniedHandler;
import laheezy.community.jwt.JwtAuthenticationEntryPoint;
import laheezy.community.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenUtils;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ExceptionHandleFilter exceptionHandleFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/v3/api-docs")
                .requestMatchers("/swagger-resources/**")
                .requestMatchers("/swagger-ui/**")
                .requestMatchers("/webjars/**")
                .requestMatchers("/swagger/**")
                .requestMatchers("/api-docs/**")
                .requestMatchers("/swagger-ui/**")
                .requestMatchers("/ws/**")
                ;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandleFilter, UsernamePasswordAuthenticationFilter.class)

                //exception handling
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않기 때문에

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll() //인증 관련 모두 통과 시킨다.
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/post").permitAll()
                .requestMatchers(HttpMethod.GET, "/member", "/member/{loginId}").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/chat/room/{roomId}").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenUtils));

        return http.build();
    }
}


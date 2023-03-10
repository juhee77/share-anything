package laheezy.community.config.jwt;

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
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenUtils;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

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
                ;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF ?????? Disable
        http.csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

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
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // ?????????, ???????????? API ??? ????????? ?????? ???????????? ????????? ???????????? ????????? permitAll ??????
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll() //?????? ?????? ?????? ?????? ?????????.
                .requestMatchers("/swagger-ui/**").permitAll()
                //.requestMatchers("/api/get-allmember").hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET,"/get-allmember","/user/{nickname}").hasAnyAuthority( "ROLE_ADMIN")
                .requestMatchers("/user").permitAll()
                .anyRequest().authenticated()   // ????????? API ??? ?????? ?????? ??????

                // JwtFilter ??? addFilterBefore ??? ???????????? JwtSecurityConfig ???????????? ??????
                .and()
                .apply(new JwtSecurityConfig(tokenUtils));

        return http.build();
    }
}


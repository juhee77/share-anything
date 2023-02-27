package laheezy.community.config.jwt;

import laheezy.community.jwt.JwtFilter;
import laheezy.community.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity> {
    private static final String[] AUTH_WHITELIST = {
            "/api/login", "/api/join", "/api/main"
    };

    private final TokenProvider tokenUtils;
    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(tokenUtils);
        builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable().cors().disable()
//                .authorizeHttpRequests(request -> request
//                        //.dispatcherTypeMatchers(AUTH_WHITELIST)
//                        .shouldFilterAllDispatcherTypes(false)
//                        .requestMatchers(AUTH_WHITELIST)
//                        .permitAll()
//                        .anyRequest().authenticated()
//                );
////                .formLogin(login -> login    // form 방식 로그인 사용
////                        .defaultSuccessUrl("/view/dashboard", true)    // 성공 시 dashboard로
////                        .permitAll()    // 대시보드 이동이 막히면 안되므로 얘는 허용
////                )
////                .logout(withDefaults());    // 로그아웃은 기본설정으로 (/logout으로 인증해제)
//
//        return http.build();
//    }
}

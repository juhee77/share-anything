package laheezy.community.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import laheezy.community.exception.CustomSecurityException;
import laheezy.community.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        //TODO 동일하게 데이터 값 처리하기
//        throw new CustomSecurityException(ErrorCode.NO_JWT_TOKEN);
    }
}
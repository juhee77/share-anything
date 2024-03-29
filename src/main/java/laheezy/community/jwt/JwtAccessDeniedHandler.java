package laheezy.community.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import laheezy.community.exception.CustomSecurityException;
import laheezy.community.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        //권환 없이 접근 하려 하는 경우 403
        //response.sendError(HttpServletResponse.SC_FORBIDDEN);
        throw new CustomSecurityException(ErrorCode.NO_AUTHORIZATION_TOKEN);
    }
}

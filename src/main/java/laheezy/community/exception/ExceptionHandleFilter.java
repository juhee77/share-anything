package laheezy.community.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//필터에 대한 예외 처리를 제공한다.
@Slf4j
@Component
public class ExceptionHandleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomSecurityException e) {
            log.error("catch Filter Exception");
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, CustomSecurityException e) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorCode errorCode = e.getErrorCode();
        log.info("error code : {}", errorCode);

        try {
            String json = new ObjectMapper().writeValueAsString(ErrorCode.of(errorCode));
            log.info("json : {}", json);
            response.setStatus(errorCode.getStatus().value()); //상태 코드로 변경
            response.getWriter().write(json);

        } catch (IOException e1) {
            e.printStackTrace();
        }
    }

}

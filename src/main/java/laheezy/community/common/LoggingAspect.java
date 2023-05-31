package laheezy.community.common;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(laheezy.community.controller..*)")
    public void restControllerMethods() {
    }

    @Before("restControllerMethods()")
    public void logRequest(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestPath = request.getRequestURI();
        String httpMethod = request.getMethod();
        String inputValues = Arrays.toString(joinPoint.getArgs());

        logger.info("API Request - Path: {}, Method: {}, Input: {}", requestPath, httpMethod, inputValues);
    }

    @AfterReturning(pointcut = "restControllerMethods()", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        String returnedValue = result != null ? result.toString() : "null";

        // Get the response status
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestPath = request.getRequestURI();
        int statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null ? (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) : HttpServletResponse.SC_OK;

        //logger.info("API Response - Status: {}, Returned Value: {}", statusCode, returnedValue);
        logger.info("API Response - Path: {}, Status: {}", requestPath, statusCode);
    }

}

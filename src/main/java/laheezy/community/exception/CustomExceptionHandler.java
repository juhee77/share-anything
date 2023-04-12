package laheezy.community.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResult> checkLogin(CustomException e) {
        log.error("[전역 컨트롤러 예외처리에서 잡음] ex", e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorResult errorResult = new ErrorResult(errorCode.getStatus().toString(), errorCode.getCode(), e.getMessage());
        //status 삭제 해야함
        return new ResponseEntity(errorResult, e.getErrorCode().getStatus());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ErrorResult checkLogin(Exception e) {
        return new ErrorResult(HttpStatus.BAD_REQUEST.toString(), "에러 코드 수정 필요!!", e.getMessage());
    }

}

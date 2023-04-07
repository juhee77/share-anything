package laheezy.community.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorResult> checkLogin(CustomException e) {
        log.error("[exceptionHandler-catchException] ex", e);
        ErrorCode errorCode = e.getErrorCode();
        ErrorResult errorResult = new ErrorResult(errorCode.getStatus().toString(),errorCode.getCode(), e.getMessage());
        //status 삭제 해야함
        return new ResponseEntity(errorResult,e.getErrorCode().getStatus());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ErrorResult checkLogin(Exception e) {
        return new ErrorResult(HttpStatus.BAD_REQUEST.toString(),"에러 코드 수정 필요!!",e.getMessage());
    }

}

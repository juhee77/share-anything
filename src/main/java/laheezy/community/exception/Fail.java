package laheezy.community.exception;

import jakarta.servlet.annotation.HttpMethodConstraint;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Fail {
    private HttpStatus status;
    private String msg;

    public Fail(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

}
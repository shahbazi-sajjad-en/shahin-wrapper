package biz.espc.shahin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CommonException extends RuntimeException {

    private final HttpStatusCode status;
    private final String message;
    private String field;
    private String rejectedValue;

    public CommonException(HttpStatusCode status, String message, String field) {
        this.status = status;
        this.message = message;
        this.field = field;
    }

    public CommonException(HttpStatusCode status, String message, String field, String rejectedValue) {
        this.status = status;
        this.message = message;
        this.field = field;
        this.rejectedValue = rejectedValue;
    }
}


package biz.espc.shahin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * Managed by subsidy
 * Create by Espc Team on 2023/07/15 20:12
 */
public class GeneralException extends RuntimeException {

    private final HttpStatus status;

    public GeneralException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}

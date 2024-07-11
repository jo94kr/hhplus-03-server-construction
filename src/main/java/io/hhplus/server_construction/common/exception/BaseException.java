package io.hhplus.server_construction.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    public BaseException(ExceptionInterface exception) {
        super(exception.getMessage());
        this.httpStatus = exception.getHttpStatus();
        this.errorCode = exception.getCode();
        this.message = exception.getMessage();
    }
}

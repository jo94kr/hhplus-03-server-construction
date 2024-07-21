package io.hhplus.server_construction.support.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionInterface {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}

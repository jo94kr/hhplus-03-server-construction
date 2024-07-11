package io.hhplus.server_construction.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionInterface {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}

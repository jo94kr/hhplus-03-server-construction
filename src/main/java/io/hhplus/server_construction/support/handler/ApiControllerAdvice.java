package io.hhplus.server_construction.support.handler;

import io.hhplus.server_construction.support.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse("500", "에러가 발생했습니다."));
    }

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ErrorResponse> handleException(BaseException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}

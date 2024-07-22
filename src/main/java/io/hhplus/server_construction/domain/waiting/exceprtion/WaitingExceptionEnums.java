package io.hhplus.server_construction.domain.waiting.exceprtion;

import io.hhplus.server_construction.support.exception.ExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WaitingExceptionEnums implements ExceptionInterface {

    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "expired token."),
    EMPTY_TOKEN(HttpStatus.BAD_REQUEST, "EMPTY_TOKEN", "Token is empty.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

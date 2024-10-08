package io.hhplus.server_construction.domain.user.exceprtion;

import io.hhplus.server_construction.support.exception.ExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionEnums implements ExceptionInterface {

    INVALID_AMOUNT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_AMOUNT_VALUE", "Invalid amount"),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "INSUFFICIENT_BALANCE", "insufficient balance")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

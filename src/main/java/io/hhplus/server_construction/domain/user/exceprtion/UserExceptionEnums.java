package io.hhplus.server_construction.domain.user.exceprtion;

import io.hhplus.server_construction.common.exception.ExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionEnums implements ExceptionInterface {

    INVALID_AMOUNT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_AMOUNT_VALUE", "Invalid amount");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

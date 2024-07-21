package io.hhplus.server_construction.domain.payment.exception;

import io.hhplus.server_construction.support.exception.ExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionEnums implements ExceptionInterface {

    INVALID_RESERVATION_STATUS(HttpStatus.BAD_REQUEST, "INVALID_RESERVATION_STATUS", "invalid reservation status"),
    INVALID_USER(HttpStatus.BAD_REQUEST, "INVALID_USER", "invalid user"),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

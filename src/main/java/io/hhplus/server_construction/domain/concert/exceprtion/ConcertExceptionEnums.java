package io.hhplus.server_construction.domain.concert.exceprtion;

import io.hhplus.server_construction.support.exception.ExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ConcertExceptionEnums implements ExceptionInterface {

    ALREADY_RESERVATION(HttpStatus.CONFLICT, "ALREADY_RESERVATION", "Seat already reserved");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

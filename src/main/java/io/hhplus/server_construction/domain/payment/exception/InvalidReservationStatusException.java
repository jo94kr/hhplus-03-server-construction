package io.hhplus.server_construction.domain.payment.exception;

import io.hhplus.server_construction.common.exception.BaseException;

public class InvalidReservationStatusException extends BaseException {

    public InvalidReservationStatusException() {
        super(PaymentExceptionEnums.INVALID_RESERVATION_STATUS);
    }
}

package io.hhplus.server_construction.domain.payment.exception;

import io.hhplus.server_construction.common.exception.BaseException;

public class InvalidUserException extends BaseException {

    public InvalidUserException() {
        super(PaymentExceptionEnums.INVALID_USER);
    }
}

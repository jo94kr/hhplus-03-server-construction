package io.hhplus.server_construction.domain.payment.exception;

import io.hhplus.server_construction.support.exception.BaseException;

public class PaymentException extends BaseException {

    public PaymentException(PaymentExceptionEnums paymentExceptionEnums) {
        super(paymentExceptionEnums);
    }
}

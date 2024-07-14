package io.hhplus.server_construction.domain.user.exceprtion;

import io.hhplus.server_construction.common.exception.BaseException;

public class ChargeAmountException extends BaseException {

    public ChargeAmountException() {
        super(UserExceptionEnums.INVALID_AMOUNT_VALUE);
    }
}

package io.hhplus.server_construction.domain.user.exceprtion;

import io.hhplus.server_construction.common.exception.BaseException;

public class UseAmountException extends BaseException {

    public UseAmountException() {
        super(UserExceptionEnums.INVALID_AMOUNT_VALUE);
    }
}

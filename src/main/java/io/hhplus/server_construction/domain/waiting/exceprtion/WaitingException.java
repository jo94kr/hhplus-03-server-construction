package io.hhplus.server_construction.domain.waiting.exceprtion;

import io.hhplus.server_construction.support.exception.BaseException;

public class WaitingException extends BaseException {

    public WaitingException(WaitingExceptionEnums waitingExceptionEnums) {
        super(waitingExceptionEnums);
    }
}

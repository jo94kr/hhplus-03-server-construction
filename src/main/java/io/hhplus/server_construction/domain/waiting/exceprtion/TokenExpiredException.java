package io.hhplus.server_construction.domain.waiting.exceprtion;

import io.hhplus.server_construction.common.exception.BaseException;

public class TokenExpiredException extends BaseException {

    public TokenExpiredException() {
        super(WaitingExceptionEnums.TOKEN_EXPIRED);
    }
}

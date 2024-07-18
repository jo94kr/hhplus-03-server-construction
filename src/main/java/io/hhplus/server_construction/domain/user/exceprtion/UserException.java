package io.hhplus.server_construction.domain.user.exceprtion;

import io.hhplus.server_construction.common.exception.BaseException;

public class UserException extends BaseException {

    public UserException(UserExceptionEnums userExceptionEnums) {
        super(userExceptionEnums);
    }
}

package io.hhplus.server_construction.domain.concert.exceprtion;

import io.hhplus.server_construction.support.exception.BaseException;

public class ConcertException extends BaseException {

    public ConcertException(ConcertExceptionEnums concertExceptionEnums) {
        super(concertExceptionEnums);
    }
}

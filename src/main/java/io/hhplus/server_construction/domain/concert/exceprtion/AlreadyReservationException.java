package io.hhplus.server_construction.domain.concert.exceprtion;

import io.hhplus.server_construction.common.exception.BaseException;

public class AlreadyReservationException extends BaseException {

    public AlreadyReservationException() {
        super(ConcertExceptionEnums.ALREADY_RESERVATION);
    }
}

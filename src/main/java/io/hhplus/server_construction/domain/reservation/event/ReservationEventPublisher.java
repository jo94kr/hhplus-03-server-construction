package io.hhplus.server_construction.domain.reservation.event;

public interface ReservationEventPublisher {

    void reservationSuccess(ReservationInfoEvent reservationInfoEvent);
}

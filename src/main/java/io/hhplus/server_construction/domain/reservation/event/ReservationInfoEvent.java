package io.hhplus.server_construction.domain.reservation.event;

import io.hhplus.server_construction.domain.reservation.Reservation;

public record ReservationInfoEvent(
        Reservation reservation
) {
}

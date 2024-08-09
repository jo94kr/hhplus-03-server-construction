package io.hhplus.server_construction.domain.data_platform.event;

import io.hhplus.server_construction.domain.reservation.Reservation;

public record ReservationInfoEvent(
        Reservation reservation
) {
}

package io.hhplus.server_construction.domain.reservation.event;

import io.hhplus.server_construction.domain.reservation.Reservation;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class ReservationInfoEvent {
    private final Reservation reservation;
    private final String outboxId;

    public ReservationInfoEvent(
            Reservation reservation,
            String outboxId
    ) {
        this.reservation = reservation;
        this.outboxId = outboxId;
    }

    public static ReservationInfoEvent create(Reservation reservation) {
        return new ReservationInfoEvent(reservation, UUID.randomUUID().toString());
    }

}

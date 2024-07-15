package io.hhplus.server_construction.application.reservation.dto;

import java.util.List;

public record ReservationConcertCommand(
        List<Long> concertSeatIdList,
        Long userId
) {
}

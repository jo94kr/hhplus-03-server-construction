package io.hhplus.server_construction.presentation.reservation.dto;

import java.math.BigDecimal;
import java.util.List;

public record ReservationConcert(

) {

    public record Request(
            List<Long> concertSeatIdList,
            Long userId
    ) {
    }

    public record Response(
            Long reservationId,
            BigDecimal totalPrice,
            List<Item> reservationItemList
    ) {

        public record Item(
                Long concertSeatId,
                String seatNum,
                BigDecimal price
        ) {
        }
    }

}
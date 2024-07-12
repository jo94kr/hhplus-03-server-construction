package io.hhplus.server_construction.presentation.reservation.dto;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertResult;
import io.hhplus.server_construction.domain.reservation.ReservationItem;

import java.math.BigDecimal;
import java.util.ArrayList;
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

        public static Response from(ReservationConcertResult reservationConcertResult) {
            return new Response(reservationConcertResult.reservationId(),
                    reservationConcertResult.totalPrice(),
                    reservationConcertResult.reservationItemList().stream()
                            .map(Item::from)
                            .toList());
        }

        public record Item(
                Long concertSeatId,
                String seatNum,
                BigDecimal price
        ) {

            public static Item from(ReservationItem reservationItem) {
                return new Item(reservationItem.getConcertSeat().getId(),
                        reservationItem.getConcertSeat().getSeatNum(),
                        reservationItem.getPrice());
            }
        }
    }

}

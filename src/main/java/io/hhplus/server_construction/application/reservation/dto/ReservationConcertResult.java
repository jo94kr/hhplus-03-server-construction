package io.hhplus.server_construction.application.reservation.dto;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;

import java.math.BigDecimal;
import java.util.List;

public record ReservationConcertResult(

        Long reservationId,
        BigDecimal totalPrice,
        List<ReservationItem> reservationItemList
) {

    public static ReservationConcertResult create(Reservation reservation) {
        return new ReservationConcertResult(reservation.getId(),
                reservation.getTotalPrice(),
                reservation.getReservationItemList());
    }

}

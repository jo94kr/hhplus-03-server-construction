package io.hhplus.server_construction.controller.reservation.dto;

import io.hhplus.server_construction.domain.reservation.ReservationEnums;

import java.math.BigDecimal;
import java.util.List;

public record PaymentDto(

) {

    public record Request(
            Long userId,
            Long reservationPaymentId
    ) {
    }

    public record Response(
            Long reservationPaymentId,
            ReservationEnums.PaymentStatus status,
            BigDecimal paymentPrice,
            BigDecimal amount
    ) {
    }

}

package io.hhplus.server_construction.application.payment.dto;

public record PaymentCommand(
        Long userId,

        Long reservationId
) {
}

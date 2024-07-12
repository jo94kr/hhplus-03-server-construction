package io.hhplus.server_construction.presentation.payment.dto;

import io.hhplus.server_construction.application.payment.dto.PaymentResult;
import io.hhplus.server_construction.domain.payment.PaymentEnums;

import java.math.BigDecimal;

public record PaymentDto(

) {

    public record Request(
            Long userId,
            Long reservationId
    ) {
    }

    public record Response(
            Long paymentId,
            PaymentEnums.PaymentStatus status,
            BigDecimal paymentPrice,
            BigDecimal amount
    ) {
        public static Response from(PaymentResult payment) {
            return new Response(payment.paymentId(), payment.status(), payment.paymentPrice(), payment.amount());
        }
    }

}

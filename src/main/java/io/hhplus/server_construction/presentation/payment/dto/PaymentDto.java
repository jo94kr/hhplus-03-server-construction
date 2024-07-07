package io.hhplus.server_construction.presentation.payment.dto;

import io.hhplus.server_construction.domain.payment.PaymentEnums;

import java.math.BigDecimal;

public record PaymentDto(

) {

    public record Request(
            Long userId,
            Long paymentId
    ) {
    }

    public record Response(
            Long paymentId,
            PaymentEnums.PaymentStatus status,
            BigDecimal paymentPrice,
            BigDecimal amount
    ) {
    }

}

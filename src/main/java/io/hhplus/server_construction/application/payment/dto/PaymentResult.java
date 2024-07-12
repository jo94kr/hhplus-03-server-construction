package io.hhplus.server_construction.application.payment.dto;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.PaymentEnums;

import java.math.BigDecimal;

public record PaymentResult(

        Long paymentId,
        PaymentEnums.PaymentStatus status,
        BigDecimal paymentPrice,
        BigDecimal amount
) {

    public static PaymentResult create(Payment payment) {
        return new PaymentResult(payment.getId(),
                payment.getStatus(),
                payment.getPrice(),
                payment.getUser().getAmount());
    }
}

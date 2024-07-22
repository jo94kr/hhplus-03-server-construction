package io.hhplus.server_construction.application.payment.dto;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.vo.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResult(

        Long paymentId,
        PaymentStatus status,
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

package io.hhplus.server_construction.presentation.payment.dto;

import io.hhplus.server_construction.application.payment.dto.PaymentResult;
import io.hhplus.server_construction.domain.payment.PaymentEnums;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PaymentDto(

) {

    public record Request(
            @Schema(name = "사용자 Id")
            Long userId,

            @Schema(name = "예약 Id")
            Long reservationId
    ) {
    }

    public record Response(
            @Schema(name = "결제 Id")
            Long paymentId,

            @Schema(name = "결제 상태")
            PaymentEnums.PaymentStatus status,

            @Schema(name = "총 결제 금액")
            BigDecimal paymentPrice,

            @Schema(name = "잔액")
            BigDecimal amount
    ) {
        public static Response from(PaymentResult payment) {
            return new Response(payment.paymentId(), payment.status(), payment.paymentPrice(), payment.amount());
        }
    }

}

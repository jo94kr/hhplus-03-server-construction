package io.hhplus.server_construction.domain.payment.event;

public record PaymentSuccessEvent(
        String token
) {
}

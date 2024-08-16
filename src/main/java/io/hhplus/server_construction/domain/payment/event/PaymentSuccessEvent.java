package io.hhplus.server_construction.domain.payment.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public final class PaymentSuccessEvent {
    private final String token;
    private final String outboxId;

    public PaymentSuccessEvent(
            String token,
            String outboxId
    ) {
        this.token = token;
        this.outboxId = outboxId;
    }

    public static PaymentSuccessEvent create(String token) {
        return new PaymentSuccessEvent(token, UUID.randomUUID().toString());
    }
}

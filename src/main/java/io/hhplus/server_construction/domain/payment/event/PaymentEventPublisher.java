package io.hhplus.server_construction.domain.payment.event;

public interface PaymentEventPublisher {

    void paymentSuccess(PaymentSuccessEvent paymentSuccessEvent);
}

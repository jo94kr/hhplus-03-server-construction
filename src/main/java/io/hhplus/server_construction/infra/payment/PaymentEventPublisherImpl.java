package io.hhplus.server_construction.infra.payment;

import io.hhplus.server_construction.domain.payment.event.PaymentEventPublisher;
import io.hhplus.server_construction.domain.payment.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void paymentSuccess(PaymentSuccessEvent paymentSuccessEvent) {
        applicationEventPublisher.publishEvent(paymentSuccessEvent);
    }
}

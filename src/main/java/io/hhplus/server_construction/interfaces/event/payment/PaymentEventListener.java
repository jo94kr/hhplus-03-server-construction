package io.hhplus.server_construction.interfaces.event.payment;

import io.hhplus.server_construction.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final WaitingService waitingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tokenExpireHandler(PaymentSuccessEvent event) {
        // 토큰 만료 처리
        waitingService.expiredToken(event.token());
    }
}

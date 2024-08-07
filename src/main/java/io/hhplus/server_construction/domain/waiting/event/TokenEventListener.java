package io.hhplus.server_construction.domain.waiting.event;

import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TokenEventListener {

    private final WaitingService waitingService;

    public TokenEventListener(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tokenExpireHandler(TokenExpireEvent event) {
        // 토큰 만료 처리
        waitingService.expiredToken(event.token());
    }
}

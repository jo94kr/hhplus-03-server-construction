package io.hhplus.server_construction.domain.waiting.event;

import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TokenEventListener {

    private final WaitingService waitingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tokenExpireHandler(TokenExpireEvent event) {
        // 토큰 만료 처리
        waitingService.expiredToken(event.token());
    }
}

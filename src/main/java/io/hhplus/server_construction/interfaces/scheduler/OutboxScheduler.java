package io.hhplus.server_construction.interfaces.scheduler;

import io.hhplus.server_construction.application.outbox.facade.OutboxFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxFacade outboxFacade;

    // 5분마다 실행
    @Scheduled(cron = "0 */5 * * * *")
    public void run() {
        outboxFacade.retryOutboxMessage(LocalDateTime.now().minusMinutes(5));
    }
}

package io.hhplus.server_construction.interfaces.scheduler;


import io.hhplus.server_construction.application.waiting.facade.WaitingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingScheduler {

    private final WaitingFacade waitingFacade;

    // 10초마다 실행
    @Scheduled(cron = "*/10 * * * * *")
    public void run() {
        waitingFacade.tokenScheduler();
    }
}

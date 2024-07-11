package io.hhplus.server_construction.application;


import io.hhplus.server_construction.application.facade.WaitingFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingScheduler {

    private final WaitingFacade waitingFacade;

    @Scheduled(fixedRate = 10000)
    public void run() {
        waitingFacade.tokenScheduler();
    }
}
package io.hhplus.server_construction.application.waiting.facade;

import io.hhplus.server_construction.application.waiting.dto.CheckTokenResult;
import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingFacade {

    private final WaitingService waitingService;

    public CheckTokenResult checkToken(String token) {
        // 대기열 체크
        Waiting waiting = waitingService.checkToken(token);
        return CheckTokenResult.create(waiting.getToken(),
                waiting.isAvailableToken() ? null : waiting.getTimeRemainingMinutes(),
                waiting.getRank(),
                waiting.getStatus(),
                waiting.getExpiredDatetime());
    }

    public void auth(String token) {
        waitingService.checkWaitingStatus(token);
    }

    public void tokenScheduler() {
        // 대기열 진입 가능한 토큰 상태 변경
        waitingService.activeToken();
    }

    public void expiredToken(String token) {
        waitingService.expiredToken(token);
    }
}

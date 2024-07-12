package io.hhplus.server_construction.application.waiting.facade;

import io.hhplus.server_construction.application.waiting.dto.CheckTokenResult;
import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class WaitingFacade {

    private final WaitingService waitingService;

    @Transactional(rollbackFor = {Exception.class})
    public CheckTokenResult checkToken(String token) {
        // 대기열 체크
        Waiting waiting = waitingService.checkToken(token);

        // 대기열 순번
        Long waitingNumber = waitingService.calcWaitingNumber(waiting);

        // 남은 시간(분)
        Long timeRemainingMinutes = waitingService.calcTimeRemaining(waiting, waitingNumber);

        return CheckTokenResult.create(waiting.getToken(),
                timeRemainingMinutes,
                waitingNumber,
                waiting.getStatus(),
                waiting.getExpiredDatetime());
    }

    @Transactional(rollbackFor = {Exception.class})
    public void tokenScheduler() {
        LocalDateTime now = LocalDateTime.now();
        // 만료 일시가 5분이 넘은 토큰 만료 처리
        waitingService.expiredToken(now);

        // 대기열 진입 가능한 토큰 상태 변경
        waitingService.activeToken(now);
    }
}
package io.hhplus.server_construction.domain.data_platform.event;

import io.hhplus.server_construction.domain.data_platform.service.DataPlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPlatformEventListener {

    private final DataPlatformService dataPlatformService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationDataSendHandler(ReservationInfoEvent event) {
        try {
            // 데이터 플랫폼 전송
            dataPlatformService.sendReservationInfo(event.reservation());
        } catch (InterruptedException e) {
            log.error("send payment info error", e);
        }
    }
}

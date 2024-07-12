package io.hhplus.server_construction.application.facade;

import io.hhplus.server_construction.application.waiting.dto.CheckTokenResult;
import io.hhplus.server_construction.application.waiting.facade.WaitingFacade;
import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingFacadeTest {

    @InjectMocks
    private WaitingFacade waitingFacade;

    @Mock
    private WaitingService waitingService;

    @Test
    @DisplayName("토큰이 없을 경우 토큰을 발급한다.")
    void createToken() {
        // given
        Waiting waiting = Waiting.create();

        // when
        when(waitingService.checkToken(null)).thenReturn(waiting);
        when(waitingService.calcWaitingNumber(waiting)).thenReturn(any());
        when(waitingService.calcTimeRemaining(waiting, any())).thenReturn(any());
        CheckTokenResult result = waitingFacade.checkToken(null);

        // then
        assertThat(result.status()).isEqualTo(WaitingStatus.WAITING);
    }

    @Test
    @DisplayName("토큰이 있을 경우 대기열 정보를 조회한다.")
    void updateTokenExpirationDate() {
        // given
        Waiting waiting = Waiting.create();
        String token = "DUMMY_TOKEN";
        long waitingNumber = 10L;
        long timeRemainingMinutes = 10L;

        // when
        when(waitingService.checkToken(token)).thenReturn(waiting);
        when(waitingService.calcWaitingNumber(waiting)).thenReturn(waitingNumber);
        when(waitingService.calcTimeRemaining(waiting, waitingNumber)).thenReturn(timeRemainingMinutes);
        CheckTokenResult result = waitingFacade.checkToken(token);

        // then
        assertNotNull(result);
    }
}

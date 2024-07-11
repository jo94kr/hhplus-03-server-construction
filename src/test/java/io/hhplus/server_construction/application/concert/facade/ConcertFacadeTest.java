package io.hhplus.server_construction.application.concert.facade;

import io.hhplus.server_construction.domain.waiting.exceprtion.TokenExpiredException;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertFacadeTest {

    @InjectMocks
    private ConcertFacade concertFacade;

    @Mock
    private WaitingService waitingService;

    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 조회")
    void findConcertListTokenException() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(false);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> concertFacade.findConcertList(pageRequest, TOKEN);

        // then
        assertThatExceptionOfType(TokenExpiredException.class).isThrownBy(throwingCallable);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 스케쥴 조회")
    void findConcertScheduleListTokenException() {
        // given
        Long concertId = 1L;

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(false);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> concertFacade.findConcertScheduleList(concertId, TOKEN, any(), any());

        // then
        assertThatExceptionOfType(TokenExpiredException.class).isThrownBy(throwingCallable);
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 좌석 조회")
    void findConcertSeatListTokenException() {
        // given
        Long concertId = 1L;
        Long concertScheduleId = 1L;

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(false);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> concertFacade.findConcertSeatList(concertId, concertScheduleId, TOKEN);

        // then
        assertThatExceptionOfType(TokenExpiredException.class).isThrownBy(throwingCallable);
    }
}

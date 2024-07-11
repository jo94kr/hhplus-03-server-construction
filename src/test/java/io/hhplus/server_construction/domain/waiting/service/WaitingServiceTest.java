package io.hhplus.server_construction.domain.waiting.service;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.exceprtion.TokenExpiredException;
import io.hhplus.server_construction.domain.waiting.repoisitory.WaitingRepository;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @InjectMocks
    private WaitingService waitingService;

    @Mock
    private WaitingRepository waitingRepository;

    @Test
    @DisplayName("토큰이 있고 아직 입장하지 못한다면 대기열 만료일시가 갱신된다.")
    void renewalExpiredDatetime() {
        // given
        Waiting waiting = Waiting.create();
        String token = waiting.getToken();
        LocalDateTime expiredDatetime = waiting.getExpiredDatetime();

        // when
        when(waitingRepository.findWaitingByToken(token)).thenReturn(waiting);
        Waiting result = waitingService.checkToken(token);

        // then
        assertThat(result.getExpiredDatetime()).isAfter(expiredDatetime);
    }

    @Test
    @DisplayName("만료된 토큰조회시 예외처리")
    void expiredWaitingToken() {
        // given
        Waiting waiting = Waiting.create().expireToken();
        String token = waiting.getToken();

        // when
        when(waitingRepository.findWaitingByToken(token)).thenReturn(waiting);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> waitingService.checkToken(token);

        // then
        assertThatExceptionOfType(TokenExpiredException.class).isThrownBy(throwingCallable);
    }

    @Test
    @DisplayName("대기순번을 계산한다.")
    void calcWaitingNumber() {
        // given
        Long lastProceedingId = 5L;
        Long currentWaitingId = 15L;
        LocalDateTime now = LocalDateTime.now();
        Waiting waiting = new Waiting(currentWaitingId,
                "DUMMY_TOKEN",
                WaitingStatus.WAITING,
                null,
                now,
                now);

        // when
        when(waitingRepository.findLastProceedingWaiting()).thenReturn(lastProceedingId);
        Long waitingNumber = waitingService.calcWaitingNumber(waiting);

        // then
        assertThat(waitingNumber).isEqualTo(currentWaitingId - lastProceedingId);
    }

    @Test
    @DisplayName("남은 대기 시간을 계산한다.")
    void calcTimeRemaining() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Waiting waiting = new Waiting(100L,
                "DUMMY_TOKEN",
                WaitingStatus.WAITING,
                null,
                now,
                now);
        // 대기 번호
        Long waitingNumber = 10L;
        // 분당 처리량
        Long throughputPerMinute = 5L;
        // 임의로 지정한 프로세스 처리 시간(분)
        long cycleTime = 5L;

        // when
        when(waitingRepository.findThroughputPerMinute(now.minusMinutes(1))).thenReturn(throughputPerMinute);
        Long result = waitingService.calcTimeRemaining(waiting, waitingNumber, now);

        // then
        // 남은 시간 = (대기번호 / 분당 처리량) * 임의로 지정한 프로세스 처리 시간(분)
        long timeRemaining = (waitingNumber / throughputPerMinute) * cycleTime;
        assertThat(result).isEqualTo(timeRemaining);
        assertThat(waiting.getRemainingDatetime()).isNotNull();
    }

    @Test
    @DisplayName("스케쥴러 - 대기열 만료 시간이 지난 대기열을 만료시킨다.")
    void expiredToken() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<Waiting> waitingList = List.of(
                new Waiting(1L,
                        "DUMMY_TOKEN_1",
                        WaitingStatus.WAITING,
                        null,
                        now.minusMinutes(10),
                        now),
                new Waiting(2L,
                        "DUMMY_TOKEN_2",
                        WaitingStatus.WAITING,
                        null,
                        now.minusMinutes(10),
                        now)
        );

        // when
        when(waitingRepository.findWaitingByStatusAndExpireDatetimeIsBefore(WaitingStatus.WAITING, now.minusMinutes(5)))
                .thenReturn(waitingList);
        waitingService.expiredToken(now);

        // then
        assertThat(waitingList).allSatisfy(waiting -> assertThat(waiting.getStatus()).isEqualTo(WaitingStatus.EXPIRED));
    }

    @Test
    @DisplayName("스케쥴러 - 진입 가능 시간에 도달한 대기열의 상태를 바꾼다.")
    void activeToken() {
        // given
        LocalDateTime now = LocalDateTime.now();
        List<Waiting> waitingList = List.of(
                new Waiting(1L,
                        "DUMMY_TOKEN_1",
                        WaitingStatus.WAITING,
                        now,
                        now,
                        now),
                new Waiting(2L,
                        "DUMMY_TOKEN_2",
                        WaitingStatus.WAITING,
                        now,
                        now,
                        now)
        );

        // when
        when(waitingRepository.findWaitingByStatusAndRemainingDatetimeIsBefore(WaitingStatus.WAITING, now))
                .thenReturn(waitingList);
        waitingService.activeToken(now);

        // then
        assertThat(waitingList).allSatisfy(waiting -> assertThat(waiting.getStatus()).isEqualTo(WaitingStatus.PROCEEDING));
    }

    @Test
    @DisplayName("사용 가능한 토큰인지 체크한다.")
    void checkWaitingStatus() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String token = "DUMMY_TOKEN";
        Waiting waiting = new Waiting(1L, token, WaitingStatus.PROCEEDING, now, now, now);

        // when
        when(waitingRepository.findWaitingByToken(token)).thenReturn(waiting);
        boolean isAvailable = waitingService.checkWaitingStatus(token);

        // then
        assertThat(isAvailable).isTrue();
    }
}

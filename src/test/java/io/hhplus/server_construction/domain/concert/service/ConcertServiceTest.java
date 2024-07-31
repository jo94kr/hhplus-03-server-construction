package io.hhplus.server_construction.domain.concert.service;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertException;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertExceptionEnums;
import io.hhplus.server_construction.domain.concert.repoisitory.ConcertRepository;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleStatus;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @InjectMocks
    private ConcertService concertService;

    @Mock
    private ConcertRepository concertRepository;

    @Test
    @DisplayName("좌석을 임시 예약 상태로 변환한다")
    void temporaryChangeTemporarySeat() {
        // given
        Long concertSeatId = 1L;
        Concert concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());
        ConcertSeat concertSeat = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());

        // when
        when(concertRepository.findById(concertSeatId)).thenReturn(concertSeat);
        when(concertRepository.saveConcertSeat(concertSeat)).thenReturn(concertSeat);
        List<ConcertSeat> concertSeatList = concertService.setSeatReservation(List.of(1L));

        // then
        assertThat(concertSeatList).isNotEmpty()
                .allSatisfy(c -> assertThat(c.getStatus()).isEqualTo(ConcertSeatStatus.PENDING));
    }

    @Test
    @DisplayName("이미 임시예약/예약완료된 좌석 호출시 예외")
    void alreadyReservationException() {
        // given
        Long concertSeatId = 1L;
        Concert concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());
        ConcertSeat concertSeat = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now());

        // when
        when(concertRepository.findById(concertSeatId)).thenReturn(concertSeat);

        // then
        assertThatThrownBy(() -> concertService.setSeatReservation(List.of(1L)))
                .isInstanceOf(ConcertException.class)
                .hasMessageContaining(ConcertExceptionEnums.ALREADY_RESERVATION.getMessage());
    }
}

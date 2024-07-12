package io.hhplus.server_construction.domain.reservation.service;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleEnums;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatEnums;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatusEnums;
import io.hhplus.server_construction.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
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
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private User user;
    private Concert concert;
    private ConcertSchedule concertSchedule;
    private ConcertSeat concertSeat1;
    private ConcertSeat concertSeat2;
    private ConcertSeat concertSeat3;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        this.user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now);

        this.concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());

        this.concertSchedule = ConcertSchedule.create(1L,
                this.concert,
                LocalDateTime.now(),
                ConcertScheduleEnums.ScheduleStatus.AVAILABLE,
                LocalDateTime.now(),
                LocalDateTime.now());

        this.concertSeat1 = ConcertSeat.create(1L,
                this.concertSchedule,
                "A1",
                ConcertSeatEnums.Grade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatEnums.Status.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
        this.concertSeat2 = ConcertSeat.create(2L,
                this.concertSchedule,
                "A2",
                ConcertSeatEnums.Grade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatEnums.Status.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
        this.concertSeat3 = ConcertSeat.create(3L,
                this.concertSchedule,
                "A3",
                ConcertSeatEnums.Grade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatEnums.Status.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    @DisplayName("콘서트를 예약한다. - 좌석의 총 합계 계산")
    void reservationConcert() {
        // given
        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Reservation reservation = Reservation.create(any(), user, ReservationStatusEnums.PAYMENT_WAITING, totalPrice);

        // when
        when(reservationRepository.saveReservation(reservation)).thenReturn(reservation);
        when(reservationRepository.saveAllReservationItems(any())).thenReturn(any());
        Reservation result = reservationService.reservationConcert(concertSeatList, this.user);

        // then
        assertThat(result.getTotalPrice()).isEqualTo(totalPrice);
    }
}

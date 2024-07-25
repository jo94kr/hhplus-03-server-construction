package io.hhplus.server_construction.domain.reservation.service;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleStatus;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
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
    private ConcertSeat concertSeat1;
    private ConcertSeat concertSeat2;
    private ConcertSeat concertSeat3;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        this.user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now);

        Concert concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());

        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                LocalDateTime.now(),
                ConcertScheduleStatus.AVAILABLE,
                LocalDateTime.now(),
                LocalDateTime.now());

        this.concertSeat1 = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
        this.concertSeat2 = ConcertSeat.create(2L,
                concertSchedule,
                "A2",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
        this.concertSeat3 = ConcertSeat.create(3L,
                concertSchedule,
                "A3",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    @DisplayName("콘서트를 예약한다. - 좌석의 총 합계 계산")
    void setConcertReservation() {
        // given
        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Reservation reservation = Reservation.create(any(), user, ReservationStatus.PAYMENT_WAITING, totalPrice, 0L);

        // when
        when(reservationRepository.saveReservation(reservation)).thenReturn(reservation);
        when(reservationRepository.saveAllReservationItems(any())).thenReturn(any());
        Reservation result = reservationService.setConcertReservation(concertSeatList, this.user);

        // then
        assertThat(result.getTotalPrice()).isEqualTo(totalPrice);
    }


}

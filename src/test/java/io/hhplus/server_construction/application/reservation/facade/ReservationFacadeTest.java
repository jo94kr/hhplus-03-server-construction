package io.hhplus.server_construction.application.reservation.facade;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.application.reservation.dto.ReservationConcertResult;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertException;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertExceptionEnums;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleStatus;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @InjectMocks
    private ReservationFacade reservationFacade;

    @Mock
    private UserService userService;

    @Mock
    private ConcertService concertService;

    @Mock
    private ReservationService reservationService;

    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 좌석을 예약한다.")
    void setConcertReservationTokenException() {
        // given
        Long userId = 1L;
        ReservationConcertCommand reservationConcertCommand = new ReservationConcertCommand(List.of(1L, 2L, 3L), userId);

        LocalDateTime now = LocalDateTime.now();
        Concert concert = Concert.create(1L, "항해 콘서트", now, now);
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                now,
                ConcertScheduleStatus.AVAILABLE,
                now,
                now);

        List<ConcertSeat> concertSeatList = List.of(ConcertSeat.create(1L,
                concertSchedule,
                "A01",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                0L,
                now,
                now));

        User user = User.create(1L,
                "조진우",
                BigDecimal.valueOf(10000),
                now,
                now);

        // when
        when(userService.findUserById(reservationConcertCommand.userId())).thenReturn(user);
        when(concertService.setSeatReservation(reservationConcertCommand.concertSeatIdList())).thenReturn(concertSeatList);
        when(reservationService.setConcertReservation(concertSeatList, user)).thenReturn(Reservation.create(
                1L,
                user,
                BigDecimal.valueOf(3000),
                ReservationStatus.PAYMENT_WAITING,
                anyList(),
                now
        ));
        ReservationConcertResult result = reservationFacade.setConcertReservation(reservationConcertCommand);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유효한 상태가 아닌 콘서트를 예약한다.")
    void setConcertReservationNotPossible() {
        // given
        Long userId = 1L;
        List<Long> concertSeatIdList = List.of(1L, 2L, 3L);
        ReservationConcertCommand reservationConcertCommand = new ReservationConcertCommand(concertSeatIdList, userId);

        // when
        when(userService.findUserById(userId)).thenReturn(any());
        when(concertService.setSeatReservation(concertSeatIdList)).thenThrow(new ConcertException(ConcertExceptionEnums.ALREADY_RESERVATION));

        // then
        assertThatThrownBy(() -> reservationFacade.setConcertReservation(reservationConcertCommand))
                .isInstanceOf(ConcertException.class)
                .hasMessageContaining(ConcertExceptionEnums.ALREADY_RESERVATION.getMessage());
    }
}

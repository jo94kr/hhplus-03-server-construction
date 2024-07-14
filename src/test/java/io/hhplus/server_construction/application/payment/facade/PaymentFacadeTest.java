package io.hhplus.server_construction.application.payment.facade;

import io.hhplus.server_construction.application.payment.dto.PaymentResult;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleEnums;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatEnums;
import io.hhplus.server_construction.domain.payment.exception.InvalidReservationStatusException;
import io.hhplus.server_construction.domain.payment.exception.InvalidUserException;
import io.hhplus.server_construction.domain.payment.service.PaymentService;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatusEnums;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.exceprtion.UseAmountException;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeTest {

    @InjectMocks
    private PaymentFacade paymentFacade;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private UserService userService;

    @Mock
    private WaitingService waitingService;

    private static final String TOKEN = "DUMMY_TOKEN";
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
                ConcertScheduleEnums.ScheduleStatus.AVAILABLE,
                LocalDateTime.now(),
                LocalDateTime.now());

        this.concertSeat1 = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatEnums.Grade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatEnums.Status.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
        this.concertSeat2 = ConcertSeat.create(2L,
                concertSchedule,
                "A2",
                ConcertSeatEnums.Grade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatEnums.Status.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
        this.concertSeat3 = ConcertSeat.create(3L,
                concertSchedule,
                "A3",
                ConcertSeatEnums.Grade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatEnums.Status.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    @DisplayName("결제가 불가능한 예약을 결제할 경우 예외")
    void payment() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;

        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = Reservation.create(any(), user, ReservationStatusEnums.CANCEL, totalPrice);

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(true);
        when(reservationService.findReservationWithItemListById(reservationId)).thenReturn(reservation);

        // then
        assertThatThrownBy(() -> paymentFacade.payment(reservationId, userId, TOKEN))
                .isInstanceOf(InvalidReservationStatusException.class);
    }

    @Test
    @DisplayName("잔액이 부족한 사용자 결제 요청")
    void insufficientBalanceException() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(100L), now, now);

        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = Reservation.create(any(), user, ReservationStatusEnums.PAYMENT_WAITING, totalPrice);

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(true);
        when(reservationService.findReservationWithItemListById(reservationId)).thenReturn(reservation);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userService.use(user, totalPrice)).thenThrow(UseAmountException.class);

        // then
        assertThatThrownBy(() -> paymentFacade.payment(reservationId, userId, TOKEN))
                .isInstanceOf(UseAmountException.class);
    }

    @Test
    @DisplayName("예약자와 다른 사용자가 결제 요청")
    void unMatchUserException() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;
        LocalDateTime now = LocalDateTime.now();
        User user2 = User.create(2L, "다른사람", BigDecimal.valueOf(10000L), now, now);

        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = Reservation.create(any(), user, ReservationStatusEnums.PAYMENT_WAITING, totalPrice);

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(true);
        when(reservationService.findReservationWithItemListById(reservationId)).thenReturn(reservation);
        when(userService.findUserById(userId)).thenReturn(user2);
        when(paymentService.payment(reservation, user2)).thenThrow(InvalidUserException.class);

        // then
        assertThatThrownBy(() -> paymentFacade.payment(reservationId, userId, TOKEN))
                .isInstanceOf(InvalidUserException.class);
    }
}

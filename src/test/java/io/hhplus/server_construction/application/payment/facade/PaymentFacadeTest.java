package io.hhplus.server_construction.application.payment.facade;

import io.hhplus.server_construction.application.payment.dto.PaymentCommand;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleStatus;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.domain.payment.exception.PaymentException;
import io.hhplus.server_construction.domain.payment.exception.PaymentExceptionEnums;
import io.hhplus.server_construction.domain.payment.service.PaymentService;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.exceprtion.UserException;
import io.hhplus.server_construction.domain.user.exceprtion.UserExceptionEnums;
import io.hhplus.server_construction.domain.user.service.UserService;
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
class PaymentFacadeTest {

    @InjectMocks
    private PaymentFacade paymentFacade;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private UserService userService;

    private static final String TOKEN = "DUMMY_TOKEN";
    private User user;
    private ConcertSeat concertSeat1;
    private ConcertSeat concertSeat2;
    private ConcertSeat concertSeat3;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        this.user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now, 0L);

        Concert concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());

        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                now,
                ConcertScheduleStatus.AVAILABLE,
                now,
                now);

        this.concertSeat1 = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                now,
                now);
        this.concertSeat2 = ConcertSeat.create(2L,
                concertSchedule,
                "A2",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                now,
                now);
        this.concertSeat3 = ConcertSeat.create(3L,
                concertSchedule,
                "A3",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                now,
                now);
    }

    @Test
    @DisplayName("결제가 불가능한 예약을 결제할 경우 예외")
    void payment() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;
        PaymentCommand paymentCommand = new PaymentCommand(userId, reservationId);

        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = Reservation.create(any(), user, ReservationStatus.CANCEL, totalPrice);

        // when
        when(reservationService.findReservationWithItemListById(reservationId)).thenReturn(reservation);

        // then
        assertThatThrownBy(() -> paymentFacade.payment(paymentCommand, TOKEN))
                .isInstanceOf(PaymentException.class);
    }

    @Test
    @DisplayName("잔액이 부족한 사용자 결제 요청")
    void insufficientBalanceException() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;
        PaymentCommand paymentCommand = new PaymentCommand(userId, reservationId);
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(100L), now, now, 0L);

        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = Reservation.create(any(), user, ReservationStatus.PAYMENT_WAITING, totalPrice);

        // when
        when(reservationService.findReservationWithItemListById(reservationId)).thenReturn(reservation);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userService.use(user, totalPrice)).thenThrow(new UserException(UserExceptionEnums.INVALID_AMOUNT_VALUE));

        // then
        assertThatThrownBy(() -> paymentFacade.payment(paymentCommand, TOKEN))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserExceptionEnums.INVALID_AMOUNT_VALUE.getMessage());
    }

    @Test
    @DisplayName("예약자와 다른 사용자가 결제 요청")
    void unMatchUserException() {
        // given
        Long userId = 1L;
        Long reservationId = 1L;
        PaymentCommand paymentCommand = new PaymentCommand(userId, reservationId);
        LocalDateTime now = LocalDateTime.now();
        User user2 = User.create(2L, "다른사람", BigDecimal.valueOf(10000L), now, now, 0L);

        List<ConcertSeat> concertSeatList = List.of(this.concertSeat1, this.concertSeat2, this.concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = Reservation.create(any(), user, ReservationStatus.PAYMENT_WAITING, totalPrice);

        // when
        when(reservationService.findReservationWithItemListById(reservationId)).thenReturn(reservation);
        when(userService.findUserById(userId)).thenReturn(user2);
        when(paymentService.payment(reservation, user2)).thenThrow(new PaymentException(PaymentExceptionEnums.INVALID_USER));

        // then
        assertThatThrownBy(() -> paymentFacade.payment(paymentCommand, TOKEN))
                .isInstanceOf(PaymentException.class)
                .hasMessageContaining(PaymentExceptionEnums.INVALID_USER.getMessage());
    }
}

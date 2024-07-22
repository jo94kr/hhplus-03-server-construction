package io.hhplus.server_construction.domain.payment;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleStatus;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.domain.payment.exception.PaymentException;
import io.hhplus.server_construction.domain.payment.exception.PaymentExceptionEnums;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class PaymentTest {

    @Test
    @DisplayName("주문자와 다른 사람이 주문")
    void payOtherUser() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now);
        User otherUser = User.create(2L, "다른사람", BigDecimal.valueOf(20000L), now, now);

        Concert concert = Concert.create(1L, "항해 콘서트", now, now);
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                now,
                ConcertScheduleStatus.AVAILABLE,
                now,
                now);
        ConcertSeat concertSeat1 = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                now,
                now);
        ConcertSeat concertSeat2 = ConcertSeat.create(2L,
                concertSchedule,
                "A2",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                now,
                now);
        ConcertSeat concertSeat3 = ConcertSeat.create(3L,
                concertSchedule,
                "A3",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                now,
                now);

        List<ConcertSeat> concertSeatList = List.of(concertSeat1, concertSeat2, concertSeat3);
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Reservation reservation = Reservation.create(any(), user, ReservationStatus.PAYMENT_WAITING, totalPrice);

        // when
        // then
        assertThatThrownBy(() -> Payment.pay(reservation, otherUser))
                .isInstanceOf(PaymentException.class)
                .hasMessageContaining(PaymentExceptionEnums.INVALID_USER.getMessage());
    }
}

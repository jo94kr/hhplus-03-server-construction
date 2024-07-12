package io.hhplus.server_construction.application.payment.facade;

import io.hhplus.server_construction.application.payment.dto.PaymentResult;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatEnums;
import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.exception.InvalidReservationStatusException;
import io.hhplus.server_construction.domain.payment.service.PaymentService;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatusEnums;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.domain.waiting.exceprtion.TokenExpiredException;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final WaitingService waitingService;
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final UserService userService;

    public PaymentResult payment(Long reservationId, Long userId, String token) {
        // 토큰 유효성 검사
        if (!waitingService.checkWaitingStatus(token)) {
            throw new TokenExpiredException();
        }

        // 결제 가능여부 체크
        Reservation reservation = reservationService.findReservationWithItemListById(reservationId);
        if (!reservation.isPaymentWaiting()) {
            throw new InvalidReservationStatusException();
        }

        User user = userService.findUserById(userId);
        // 결제
        userService.use(user, reservation.getTotalPrice());
        Payment payment = paymentService.payment(reservation, user);

        // 예약 상태 변경
        reservationService.save(reservation.changeStatus(ReservationStatusEnums.PAYMENT_COMPLETE));
        concertService.saveAllConcertSeat(reservation.getReservationItemList().stream()
                .map(reservationItem -> reservationItem.getConcertSeat().changeStatus(ConcertSeatEnums.Status.SOLD_OUT))
                .toList());

        // 토큰 만료 처리
        waitingService.expiredToken(token);

        return PaymentResult.create(payment);
    }
}

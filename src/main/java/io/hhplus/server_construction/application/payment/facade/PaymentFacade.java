package io.hhplus.server_construction.application.payment.facade;

import io.hhplus.server_construction.application.payment.dto.PaymentCommand;
import io.hhplus.server_construction.application.payment.dto.PaymentResult;
import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.event.PaymentEventPublisher;
import io.hhplus.server_construction.domain.payment.exception.PaymentException;
import io.hhplus.server_construction.domain.payment.exception.PaymentExceptionEnums;
import io.hhplus.server_construction.domain.payment.service.PaymentService;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.domain.payment.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public PaymentResult payment(PaymentCommand paymentCommand, String token) {
        // 결제 가능여부 체크
        Reservation reservation = reservationService.findReservationWithItemListById(paymentCommand.reservationId());
        if (!reservation.isPaymentWaiting()) {
            throw new PaymentException(PaymentExceptionEnums.INVALID_RESERVATION_STATUS);
        }

        User user = userService.findUserById(paymentCommand.userId());
        // 결제
        userService.use(user, reservation.getTotalPrice());
        Payment payment = paymentService.payment(reservation, user);

        // 예약 상태 변경
        reservationService.save(reservation.changeStatus(ReservationStatus.PAYMENT_COMPLETE));

        // 토큰 만료 처리
        paymentEventPublisher.paymentSuccess(new PaymentSuccessEvent(token));

        return PaymentResult.create(payment);
    }
}

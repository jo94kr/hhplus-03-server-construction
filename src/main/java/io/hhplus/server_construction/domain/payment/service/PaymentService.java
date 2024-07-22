package io.hhplus.server_construction.domain.payment.service;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.repository.PaymentRepository;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class PaymentService {

    private final PaymentRepository paymentRepository;

    /**
     * 결제
     * @param reservation 예약
     * @param user 사용자
     * @return Payment
     */
    @Transactional(rollbackFor = {Exception.class})
    public Payment payment(Reservation reservation, User user) {
        return paymentRepository.save(Payment.pay( reservation, user));
    }
}

package io.hhplus.server_construction.domain.payment.service;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.repository.PaymentRepository;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment payment(Reservation reservation, User user) {
        return paymentRepository.save(Payment.pay( reservation, user));
    }
}

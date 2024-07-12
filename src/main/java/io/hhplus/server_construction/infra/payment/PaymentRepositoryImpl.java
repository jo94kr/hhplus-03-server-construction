package io.hhplus.server_construction.infra.payment;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.repository.PaymentRepository;
import io.hhplus.server_construction.infra.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return PaymentMapper.toDomain(paymentJpaRepository.save(PaymentMapper.toEntity(payment)));
    }
}

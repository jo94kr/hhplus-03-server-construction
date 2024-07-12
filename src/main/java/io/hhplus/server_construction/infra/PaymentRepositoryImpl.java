package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.payment.repository.PaymentRepository;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.infra.entity.ReservationItemEntity;
import io.hhplus.server_construction.infra.mapper.PaymentMapper;
import io.hhplus.server_construction.infra.mapper.ReservationItemMapper;
import io.hhplus.server_construction.infra.mapper.ReservationMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return PaymentMapper.toDomain(paymentJpaRepository.save(PaymentMapper.toEntity(payment)));
    }
}

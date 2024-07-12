package io.hhplus.server_construction.infra.payment.mapper;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.infra.payment.entity.PaymentEntity;
import io.hhplus.server_construction.infra.reservation.mapper.ReservationMapper;
import io.hhplus.server_construction.infra.user.mapper.UserMapper;

public class PaymentMapper {

    public static Payment toDomain(PaymentEntity paymentEntity) {
        return new Payment(paymentEntity.getId(),
                ReservationMapper.toDomain(paymentEntity.getReservation()),
                UserMapper.toDomain(paymentEntity.getUser()),
                paymentEntity.getPrice(),
                paymentEntity.getStatus(),
                paymentEntity.getCreateDatetime()
        );
    }

    public static PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(payment.getId(),
                ReservationMapper.toEntity(payment.getReservation()),
                UserMapper.toEntity(payment.getUser()),
                payment.getPrice(),
                payment.getStatus(),
                payment.getCreateDatetime());
    }
}

package io.hhplus.server_construction.infra.mapper;

import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.infra.entity.PaymentEntity;
import io.hhplus.server_construction.infra.entity.UserEntity;

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

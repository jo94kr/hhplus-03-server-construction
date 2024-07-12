package io.hhplus.server_construction.infra.mapper;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.infra.entity.ReservationEntity;

public class ReservationMapper {

    public static Reservation toDomain(ReservationEntity reservationEntity) {
        return Reservation.create(reservationEntity.getId(),
                UserMapper.toDomain(reservationEntity.getUser()),
                reservationEntity.getTotalPrice());
    }

    public static ReservationEntity toEntity(Reservation reservation) {
        return new ReservationEntity(reservation.getId(),
                UserMapper.toEntity(reservation.getUser()),
                reservation.getTotalPrice());
    }
}

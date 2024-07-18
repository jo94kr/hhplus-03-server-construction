package io.hhplus.server_construction.infra.reservation.mapper;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.infra.reservation.entity.ReservationEntity;
import io.hhplus.server_construction.infra.user.mapper.UserMapper;

public class ReservationMapper {

    public static Reservation toDomain(ReservationEntity reservationEntity) {return Reservation.create(reservationEntity.getId(),
                UserMapper.toDomain(reservationEntity.getUser()),
                reservationEntity.getStatus(),
                reservationEntity.getTotalPrice());
    }

    public static ReservationEntity toEntity(Reservation reservation) {
        return new ReservationEntity(reservation.getId(),
                UserMapper.toEntity(reservation.getUser()),
                reservation.getStatus(),
                reservation.getTotalPrice());
    }
}

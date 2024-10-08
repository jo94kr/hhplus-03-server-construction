package io.hhplus.server_construction.infra.reservation.mapper;

import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.infra.concert.mapper.ConcertSeatMapper;
import io.hhplus.server_construction.infra.reservation.entity.ReservationItemEntity;

public class ReservationItemMapper {

    public static ReservationItem toDomain(ReservationItemEntity reservationItemEntity) {
        return ReservationItem.create(reservationItemEntity.getId(),
                ReservationMapper.toDomain(reservationItemEntity.getReservation()),
                ConcertSeatMapper.toDomain(reservationItemEntity.getConcertSeat()),
                reservationItemEntity.getPrice(),
                reservationItemEntity.getCreateDatetime()
        );
    }

    public static ReservationItemEntity toEntity(ReservationItem reservationItem) {
        return new ReservationItemEntity(reservationItem.getId(),
                ReservationMapper.toEntity(reservationItem.getReservation()),
                ConcertSeatMapper.toEntity(reservationItem.getConcertSeat()),
                reservationItem.getPrice());
    }
}

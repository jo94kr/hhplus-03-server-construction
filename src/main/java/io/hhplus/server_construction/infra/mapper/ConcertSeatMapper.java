package io.hhplus.server_construction.infra.mapper;

import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.infra.entity.ConcertScheduleEntity;
import io.hhplus.server_construction.infra.entity.ConcertSeatEntity;

public class ConcertSeatMapper {

    public static ConcertSeat toDomain(ConcertSeatEntity concertSeatEntity) {
        return new ConcertSeat(concertSeatEntity.getId(),
                ConcertScheduleMapper.toDomain(concertSeatEntity.getConcertSchedule()),
                concertSeatEntity.getSeatNum(),
                concertSeatEntity.getGrade(),
                concertSeatEntity.getPrice(),
                concertSeatEntity.getStatus(),
                concertSeatEntity.getCreateDatetime(),
                concertSeatEntity.getModifyDatetime());
    }

    public static ConcertSeatEntity toEntity(ConcertSeat concertSeat) {
        return new ConcertSeatEntity(concertSeat.getId(),
                ConcertScheduleMapper.toEntity(concertSeat.getConcertSchedule()),
                concertSeat.getSeatNum(),
                concertSeat.getGrade(),
                concertSeat.getPrice(),
                concertSeat.getStatus());
    }
}

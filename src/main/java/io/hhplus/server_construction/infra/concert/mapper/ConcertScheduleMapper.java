package io.hhplus.server_construction.infra.concert.mapper;

import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.infra.concert.entity.ConcertScheduleEntity;

public class ConcertScheduleMapper {

    public static ConcertSchedule toDomain(ConcertScheduleEntity concertScheduleEntity) {
        return new ConcertSchedule(concertScheduleEntity.getId(),
                ConcertMapper.toDomain(concertScheduleEntity.getConcert()),
                concertScheduleEntity.getConcertDatetime(),
                concertScheduleEntity.getCreateDatetime(),
                concertScheduleEntity.getModifyDatetime());
    }

    public static ConcertScheduleEntity toEntity(ConcertSchedule concertSchedule) {
        return new ConcertScheduleEntity(concertSchedule.getId(),
                ConcertMapper.toEntity(concertSchedule.getConcert()),
                concertSchedule.getConcertDatetime());
    }
}

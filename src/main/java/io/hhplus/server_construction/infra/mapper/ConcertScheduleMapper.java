package io.hhplus.server_construction.infra.mapper;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.infra.entity.ConcertEntity;
import io.hhplus.server_construction.infra.entity.ConcertScheduleEntity;

public class ConcertScheduleMapper {

    public static ConcertSchedule toDomain(ConcertScheduleEntity concertScheduleEntity) {
        return new ConcertSchedule(concertScheduleEntity.getId(),
                ConcertMapper.toDomain(concertScheduleEntity.getConcert()),
                concertScheduleEntity.getConcertDatetime(),
                concertScheduleEntity.getStatus(),
                concertScheduleEntity.getCreateDatetime(),
                concertScheduleEntity.getModifyDatetime());
    }

    public static ConcertScheduleEntity toEntity(ConcertSchedule concertSchedule) {
        return new ConcertScheduleEntity(concertSchedule.getId(),
                ConcertMapper.toEntity(concertSchedule.getConcert()),
                concertSchedule.getConcertDatetime(),
                concertSchedule.getStatus());
    }
}

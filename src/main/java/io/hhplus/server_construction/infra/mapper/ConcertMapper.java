package io.hhplus.server_construction.infra.mapper;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.infra.entity.ConcertEntity;
import io.hhplus.server_construction.infra.entity.WaitingEntity;

public class ConcertMapper {

    public static Concert toDomain(ConcertEntity concertEntity) {
        return new Concert(concertEntity.getId(),
                concertEntity.getName(),
                concertEntity.getCreateDatetime(),
                concertEntity.getModifyDatetime());
    }

    public static ConcertEntity toEntity(Concert concert) {
        return new ConcertEntity(concert.getId(), concert.getName());
    }
}

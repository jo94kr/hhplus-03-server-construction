package io.hhplus.server_construction.infra.mapper;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.infra.entity.WaitingEntity;

public class WaitingMapper {

    public static Waiting toDomain(WaitingEntity waitingEntity) {
        return new Waiting(waitingEntity.getId(),
                waitingEntity.getToken(),
                waitingEntity.getStatus(),
                waitingEntity.getAccessDatetime(),
                waitingEntity.getExpiredDatetime(),
                waitingEntity.getCreateDatetime());
    }

    public static WaitingEntity toEntity(Waiting waiting) {
        return new WaitingEntity(waiting.getId(),
                waiting.getToken(),
                waiting.getStatus(),
                waiting.getAccessDatetime(),
                waiting.getExpiredDatetime());
    }
}

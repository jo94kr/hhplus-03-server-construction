package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.reservation.vo.ReservationStatusEnums;
import io.hhplus.server_construction.infra.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findAllByStatusAndCreateDatetimeAfter(ReservationStatusEnums status, LocalDateTime targetDate);
}

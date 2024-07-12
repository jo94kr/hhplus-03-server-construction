package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.infra.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

}

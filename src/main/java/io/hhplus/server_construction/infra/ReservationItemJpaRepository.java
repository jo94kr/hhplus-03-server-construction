package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.infra.entity.ReservationItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationItemJpaRepository extends JpaRepository<ReservationItemEntity, Long> {

}

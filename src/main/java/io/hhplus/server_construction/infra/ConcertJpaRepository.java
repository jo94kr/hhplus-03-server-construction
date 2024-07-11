package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.infra.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long> {
}

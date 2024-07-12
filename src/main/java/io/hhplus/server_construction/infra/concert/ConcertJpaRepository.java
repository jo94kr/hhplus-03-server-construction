package io.hhplus.server_construction.infra.concert;

import io.hhplus.server_construction.infra.concert.entity.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<ConcertEntity, Long> {
}

package io.hhplus.server_construction.infra.reservation;

import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.infra.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findReservationEntityById(Long reservationId);

    List<ReservationEntity> findAllByStatusAndCreateDatetimeBefore(ReservationStatus status, LocalDateTime targetDate);
}

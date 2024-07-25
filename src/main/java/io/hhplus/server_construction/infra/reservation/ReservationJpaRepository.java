package io.hhplus.server_construction.infra.reservation;


import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.infra.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findReservationEntityById(@Param("reservationId") Long reservationId);

    List<ReservationEntity> findAllByStatusAndCreateDatetimeBefore(ReservationStatus status, LocalDateTime targetDate);
}

package io.hhplus.server_construction.infra.reservation;


import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.infra.reservation.entity.ReservationEntity;
import io.hhplus.server_construction.infra.user.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationEntity r WHERE r.id = :reservationId")
    Optional<ReservationEntity> pessimisticFindReservationById(@Param("reservationId") Long reservationId);

    List<ReservationEntity> findAllByStatusAndCreateDatetimeBefore(ReservationStatus status, LocalDateTime targetDate);
}

package io.hhplus.server_construction.infra.reservation;

import io.hhplus.server_construction.infra.reservation.entity.ReservationItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationItemJpaRepository extends JpaRepository<ReservationItemEntity, Long> {

    List<ReservationItemEntity> findByReservationId(Long reservationId);

    List<ReservationItemEntity> findByReservationIdIn(List<Long> reservationIdList);
}

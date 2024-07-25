package io.hhplus.server_construction.domain.reservation.repoisitory;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {

    Reservation saveReservation(Reservation reservation);

    List<ReservationItem> saveAllReservationItems(List<ReservationItem> reservationItemList);

    /**
     * 비관적 락 조회
     * @param reservationId 예약 Id
     * @return Reservation
     */
    Reservation findReservationById(Long reservationId);

    List<ReservationItem> findAllReservationItemByReservationId(Long reservationId);

    List<ReservationItem> findAllReservationItemByReservationIdIn(List<Long> reservationIdList);

    List<Reservation> findReservationByStatusAndTargetDate(ReservationStatus status, LocalDateTime targetDate);

    List<Reservation> saveAllReservation(List<Reservation> reservationList);
}

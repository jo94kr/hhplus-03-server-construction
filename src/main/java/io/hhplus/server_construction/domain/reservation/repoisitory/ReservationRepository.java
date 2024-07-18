package io.hhplus.server_construction.domain.reservation.repoisitory;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {

    Reservation saveReservation(Reservation reservation);

    ReservationItem saveReservationItem(ReservationItem reservationItem);

    List<ReservationItem> saveAllReservationItems(List<ReservationItem> reservationItemList);

    Reservation pessimisticFindReservationById(Long reservationId);

    List<ReservationItem> findAllReservationItemByReservationId(Long reservationId);

    List<ReservationItem> findAllReservationItemByReservationIdIn(List<Long> reservationIdList);

    List<Reservation> findReservationByStatusAndTargetDate(ReservationStatus status, LocalDateTime targetDate);

    List<Reservation> saveAllReservation(List<Reservation> reservationList);
}

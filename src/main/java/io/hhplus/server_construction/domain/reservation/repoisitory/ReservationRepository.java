package io.hhplus.server_construction.domain.reservation.repoisitory;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Reservation saveReservation(Reservation reservation);

    ReservationItem saveReservationItem(ReservationItem reservationItem);

    List<ReservationItem> saveAllReservationItems(List<ReservationItem> reservationItemList);
}

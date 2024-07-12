package io.hhplus.server_construction.domain.reservation.service;

import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation reservationConcert(List<ConcertSeat> concertSeatList, User user) {
        // 총 결제가
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = reservationRepository.saveReservation(Reservation.create(null, user, totalPrice));

        List<ReservationItem> reservationItemList = concertSeatList.stream()
                .map(concertSeat ->
                        ReservationItem.reservation(reservation, concertSeat)
                )
                .toList();
        reservationRepository.saveAllReservationItems(reservationItemList);

        return reservation.setReservationItemList(reservationItemList);
    }
}

package io.hhplus.server_construction.domain.reservation.service;

import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation save(Reservation reservation) {
        return reservationRepository.saveReservation(reservation);
    }

    public Reservation reservationConcert(List<ConcertSeat> concertSeatList, User user) {
        // 총 결제가
        BigDecimal totalPrice = concertSeatList.stream()
                .map(ConcertSeat::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Reservation reservation = reservationRepository.saveReservation(Reservation.create(null,
                user,
                ReservationStatus.PAYMENT_WAITING,
                totalPrice));

        List<ReservationItem> reservationItemList = concertSeatList.stream()
                .map(concertSeat ->
                        ReservationItem.reservation(reservation, concertSeat)
                )
                .toList();
        reservationRepository.saveAllReservationItems(reservationItemList);

        return reservation.setReservationItemList(reservationItemList);
    }

    public Reservation findReservationWithItemListById(Long reservationId) {
        Reservation reservation = reservationRepository.pessimisticFindReservationById(reservationId);
        List<ReservationItem> reservationItemList = reservationRepository.findAllReservationItemByReservationId(reservationId);
        return reservation.setReservationItemList(reservationItemList);
    }

    public List<ReservationItem> changeTemporaryReservationSeat(ReservationStatus status, LocalDateTime targetDate) {
        List<Reservation> temporaryReservationList = reservationRepository.findReservationByStatusAndTargetDate(status, targetDate);
        if (temporaryReservationList == null || temporaryReservationList.isEmpty()) {
            return new ArrayList<>();
        }
        temporaryReservationList.forEach(reservation -> reservation.changeStatus(ReservationStatus.CANCEL));
        reservationRepository.saveAllReservation(temporaryReservationList);

        return reservationRepository.findAllReservationItemByReservationIdIn(temporaryReservationList.stream()
                .map(Reservation::getId)
                .toList());
    }
}

package io.hhplus.server_construction.application.reservation.facade;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.application.reservation.dto.ReservationConcertResult;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.domain.waiting.exceprtion.TokenExpiredException;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class ReservationFacade {

    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final WaitingService waitingService;

    @Transactional(rollbackFor = {Exception.class})
    public ReservationConcertResult reservationConcert(ReservationConcertCommand concertCommand, String token) {
        if (!waitingService.checkWaitingStatus(token)) {
            throw new TokenExpiredException();
        }

        // 사용자 조회
        User user = userService.findUserById(concertCommand.userId());

        // 콘서트 좌석 조회 - 임시 예약 처리
        List<ConcertSeat> concertSeatList = concertService.reservationSeat(concertCommand.concertSeatIdList());

        // 콘서트 예약
        Reservation reservation = reservationService.reservationConcert(concertSeatList, user);

        return ReservationConcertResult.from(reservation);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void temporaryReservationSeatProcess() {
        LocalDateTime now = LocalDateTime.now();
        // 5분이 지난 미결제 예약건은 취소 처리
        List<ReservationItem> temporaryReservationItemList = reservationService.changeTemporaryReservationSeat(
                ReservationStatus.PAYMENT_WAITING,
                now.minusMinutes(5));

        // 5분이 지난 미결제 좌석은 활성화 처리
        if (!temporaryReservationItemList.isEmpty()) {
            concertService.saveAllConcertSeat(temporaryReservationItemList.stream()
                    .map(ReservationItem::getConcertSeat)
                    .map(concertSeat -> concertSeat.changeStatus(ConcertSeatStatus.POSSIBLE))
                    .toList());
        }
    }
}

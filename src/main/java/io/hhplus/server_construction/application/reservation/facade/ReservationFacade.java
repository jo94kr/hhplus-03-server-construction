package io.hhplus.server_construction.application.reservation.facade;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.application.reservation.dto.ReservationConcertResult;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.event.ReservationEventPublisher;
import io.hhplus.server_construction.domain.reservation.event.ReservationInfoEvent;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.support.aop.annotation.RedissonLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final ReservationEventPublisher reservationEventPublisher;

    @RedissonLock(value = "#reservationConcertCommand.concertSeatIdList")
    public ReservationConcertResult setConcertReservation(ReservationConcertCommand reservationConcertCommand) {
        // 사용자 조회
        User user = userService.findUserById(reservationConcertCommand.userId());

        // 콘서트 좌석 조회 - 임시 예약 처리
        List<ConcertSeat> concertSeatList = concertService.setSeatReservation(reservationConcertCommand.concertSeatIdList());

        // 콘서트 예약
        Reservation reservation = reservationService.setConcertReservation(concertSeatList, user);

        // 데이터 플랫폼 전송
        reservationEventPublisher.reservationSuccess(ReservationInfoEvent.create(reservation));

        return ReservationConcertResult.from(reservation);
    }

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

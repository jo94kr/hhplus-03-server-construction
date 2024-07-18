package io.hhplus.server_construction.application.reservation.facade;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertException;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertExceptionEnums;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.payment.exception.PaymentException;
import io.hhplus.server_construction.domain.payment.exception.PaymentExceptionEnums;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.domain.waiting.exceprtion.WaitingException;
import io.hhplus.server_construction.domain.waiting.exceprtion.WaitingExceptionEnums;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @InjectMocks
    private ReservationFacade reservationFacade;

    @Mock
    private WaitingService waitingService;

    @Mock
    private UserService userService;

    @Mock
    private ConcertService concertService;

    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 좌석을 예약한다.")
    void reservationConcertTokenException() {
        // given
        Long userId = 1L;
        ReservationConcertCommand reservationConcertCommand = new ReservationConcertCommand(List.of(1L, 2L, 3L), userId);

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(false);

        // then
        assertThatThrownBy(() -> reservationFacade.reservationConcert(reservationConcertCommand, TOKEN))
                .isInstanceOf(WaitingException.class)
                .hasMessageContaining(WaitingExceptionEnums.TOKEN_EXPIRED.getMessage());
    }

    @Test
    @DisplayName("유효한 상태가 아닌 콘서트를 예약한다.")
    void reservationConcertNotPossible() {
        // given
        Long userId = 1L;
        List<Long> concertSeatIdList = List.of(1L, 2L, 3L);
        ReservationConcertCommand reservationConcertCommand = new ReservationConcertCommand(concertSeatIdList, userId);

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(true);
        when(userService.findUserById(userId)).thenReturn(any());
        when(concertService.reservationSeat(concertSeatIdList)).thenThrow(new ConcertException(ConcertExceptionEnums.ALREADY_RESERVATION));

        // then
        assertThatThrownBy(() -> reservationFacade.reservationConcert(reservationConcertCommand, TOKEN))
                .isInstanceOf(ConcertException.class)
                .hasMessageContaining(ConcertExceptionEnums.ALREADY_RESERVATION.getMessage());
    }
}

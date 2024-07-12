package io.hhplus.server_construction.application.reservation.facade;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertCommand;
import io.hhplus.server_construction.domain.reservation.service.ReservationService;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.domain.waiting.exceprtion.TokenExpiredException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @InjectMocks
    private ReservationFacade reservationFacade;

    @Mock
    private WaitingService waitingService;

    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 좌석을 예약한다.")
    void reservationConcertTokenException() {
        // given
        Long userId = 1L;
        ReservationConcertCommand reservationConcertCommand = new ReservationConcertCommand(List.of(1L, 2L, 3L), userId, TOKEN);

        // when
        when(waitingService.checkWaitingStatus(TOKEN)).thenReturn(false);
        ThrowableAssert.ThrowingCallable throwingCallable = () -> reservationFacade.reservationConcert(reservationConcertCommand);

        // then
        assertThatExceptionOfType(TokenExpiredException.class).isThrownBy(throwingCallable);
    }
}

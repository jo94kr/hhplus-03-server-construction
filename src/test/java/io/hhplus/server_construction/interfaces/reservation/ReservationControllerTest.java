package io.hhplus.server_construction.interfaces.reservation;

import io.hhplus.server_construction.application.reservation.dto.ReservationConcertResult;
import io.hhplus.server_construction.application.reservation.facade.ReservationFacade;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertScheduleStatus;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.interfaces.controller.reservation.ReservationController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationFacade reservationFacade;

    private static final String PATH = "/reservations";
    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("콘서트 좌석을 예약한다.")
    void reservationConcert() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        String requestBody = """
                {
                    "concertSeatIdList": [1,2,3],
                    "userId": 1
                }
                """;
        Concert concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                LocalDateTime.now(),
                ConcertScheduleStatus.AVAILABLE,
                LocalDateTime.now(),
                LocalDateTime.now());
        ConcertSeat concertSeat = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                0L,
                LocalDateTime.now(),
                LocalDateTime.now());

        Reservation reservation = Reservation.create(1L,
                User.create(1L, "조진우", BigDecimal.valueOf(1000L), now, now),
                ReservationStatus.PAYMENT_WAITING,
                BigDecimal.valueOf(1000L));
        reservation.setReservationItemList(List.of(ReservationItem.create(1L,
                reservation,
                concertSeat,
                BigDecimal.valueOf(1000L),
                LocalDateTime.now())));

        // when
        when(reservationFacade.reservationConcert(any())).thenReturn(ReservationConcertResult.from(reservation));
        ResultActions response = mockMvc.perform(post(PATH)
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }
}

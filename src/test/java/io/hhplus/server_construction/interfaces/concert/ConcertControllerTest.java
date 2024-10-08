package io.hhplus.server_construction.interfaces.concert;

import io.hhplus.server_construction.application.concert.dto.FindConcertScheduleResult;
import io.hhplus.server_construction.application.concert.facade.ConcertFacade;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.interfaces.controller.concert.ConcertController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConcertController.class)
class ConcertControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ConcertFacade concertFacade;

    private static final String PATH = "/concerts";
    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("콘서트 목록을 조회한다.")
    void findConcertList() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 10);
        Concert concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());

        // when
        when(concertFacade.findConcertList(pageable)).thenReturn(new PageImpl<>(List.of(concert)));
        ResultActions response = mockMvc.perform(get(PATH)
                .header("Authorization", TOKEN)
                .param("page", "0")
                .param("size", "10"));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
        ;
    }

    @Test
    @DisplayName("콘서트 일정을 조회한다.")
    void findConcertSchedule() throws Exception {
        // given
        LocalDateTime now = LocalDateTime.now();
        Concert concert = Concert.create(1L, "항해 콘서트", now, now);
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                now,
                now,
                now);
        FindConcertScheduleResult concertScheduleResult = FindConcertScheduleResult.create(concertSchedule);

        // when
        LocalDate localDate = LocalDate.now();
        when(concertFacade.findConcertScheduleList(1L, localDate.minusDays(7), localDate)).thenReturn(List.of(concertScheduleResult));
        ResultActions response = mockMvc.perform(get(PATH + "/{concertId}/schedules", 1L)
                .header("Authorization", TOKEN)
                .param("startDate", localDate.minusDays(7).toString())
                .param("endDate", localDate.toString())
        );

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
        ;
    }

    @Test
    @DisplayName("예약 가능한 콘서트 좌석을 조회한다.")
    void findAvailableConcertSeat() throws Exception {
        // given
        Concert concert = Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now());
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());
        ConcertSeat concertSeat = ConcertSeat.create(1L,
                concertSchedule,
                "A1",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                LocalDateTime.now(),
                LocalDateTime.now());

        // when
        when(concertFacade.findAvailableConcertSeat(2L)).thenReturn(List.of(concertSeat));
        ResultActions response = mockMvc.perform(get(PATH + "/schedules/{concertScheduleId}/seats", 2L)
                .header("Authorization", TOKEN));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
        ;
    }
}

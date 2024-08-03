package io.hhplus.server_construction.application.concert.facade;

import io.hhplus.server_construction.application.concert.dto.FindConcertScheduleResult;
import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.service.ConcertService;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatGrade;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertFacadeTest {

    @InjectMocks
    private ConcertFacade concertFacade;

    @Mock
    private ConcertService concertService;

    @Test
    @DisplayName("콘서트 조회")
    void findConcertList() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<Concert> concerts = new PageImpl<>(List.of(Concert.create(1L, "항해 콘서트", LocalDateTime.now(), LocalDateTime.now())));

        // when
        when(concertService.findConcertListWithCache(pageRequest)).thenReturn(concerts);
        Page<Concert> concertList = concertFacade.findConcertList(pageRequest);

        // then
        assertThat(concertList).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 스케쥴 조회")
    void findConcertScheduleListTokenException() {
        // given
        Long concertId = 1L;
        Concert concert = Concert.create(1L, "항해 콘서트", any(), any());
        List<ConcertSchedule> concertScheduleList = List.of(ConcertSchedule.create(1L,
                concert,
                any(),
                any(),
                any()));
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        // when
        when(concertService.findConcertScheduleList(concertId, startDate, endDate)).thenReturn(concertScheduleList);
        List<FindConcertScheduleResult> resultList = concertFacade.findConcertScheduleList(concertId, startDate, endDate);

        // then
        assertThat(resultList).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 콘서트 좌석 조회")
    void findConcertSeatListTokenException() {
        // given
        Long concertId = 1L;
        Long concertScheduleId = 1L;

        LocalDateTime now = LocalDateTime.now();
        Concert concert = Concert.create(1L, "항해 콘서트", now, now);
        ConcertSchedule concertSchedule = ConcertSchedule.create(1L,
                concert,
                now,
                now,
                now);

        List<ConcertSeat> concertSeatList = List.of(ConcertSeat.create(1L,
                concertSchedule,
                "A01",
                ConcertSeatGrade.GOLD,
                BigDecimal.valueOf(1000),
                ConcertSeatStatus.POSSIBLE,
                now,
                now));

        // when
        when(concertService.findAllConcertSeatList(concertId, concertScheduleId)).thenReturn(concertSeatList);
        List<ConcertSeat> resultList = concertFacade.findConcertSeatList(concertId, concertScheduleId);

        // then
        assertThat(resultList).isNotNull();
    }
}

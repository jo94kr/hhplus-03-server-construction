package io.hhplus.server_construction.interfaces.reservation;

import io.hhplus.server_construction.IntegratedTest;
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
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {
        "classpath:/db/create_waiting.sql",
        "classpath:/db/create_user.sql",
        "classpath:/db/create_concert.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerIntegratedTest extends IntegratedTest {

    private static final String PATH = "/reservations";

    @Test
    @DisplayName("콘서트 좌석을 예약한다.")
    void reservationConcert() {
        // given
        String requestBody = """
                {
                    "concertSeatIdList": [1, 2],
                    "userId": 1
                }
                """;
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_2");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post(PATH)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

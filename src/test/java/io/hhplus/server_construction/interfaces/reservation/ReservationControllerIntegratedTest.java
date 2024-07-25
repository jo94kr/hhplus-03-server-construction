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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
    @DisplayName("만료된 토큰으로 콘서트 좌석을 예약한다.")
    void reservationConcertByExpiredToken() {
        // given
        String requestBody = """
                {
                    "concertSeatIdList": [1, 2],
                    "userId": 1
                }
                """;
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_1");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post(PATH)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

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

    @Test
    @DisplayName("동일한 사용자가 동시에 좌석 예약 요청")
    void seatReservationRequestsBySameTime() {
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
        int cnt = 2;
        CompletableFuture<Integer>[] futureArray = new CompletableFuture[cnt];
        for (int i = 0; i < cnt; i++) {
            futureArray[i] = CompletableFuture.supplyAsync(() -> {
                ExtractableResponse<Response> response = RestAssured
                        .given().log().all()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(requestBody)
                        .when().post(PATH)
                        .then().log().all().extract();
                return response.statusCode();
            });
        }
        CompletableFuture.allOf(futureArray).join();

        // then
        List<Integer> failCnt = Arrays.stream(futureArray)
                .map(CompletableFuture::join)
                .filter(statusCode -> statusCode != HttpStatus.OK.value())
                .toList();

        assertThat(failCnt).hasSize(1);
    }

    @Test
    @DisplayName("다른 사용자가 동시에 같은 좌석 예약 신청")
    void seatReservationRequestsBySameTimeAndOtherUser() {
        // given
        String requestBody1 = """
            {
                "concertSeatIdList": [1, 2],
                "userId": 1
            }
            """;
        String requestBody2 = """
            {
                "concertSeatIdList": [1],
                "userId": 2
            }
            """;

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_2");

        // when
        CompletableFuture<Integer>[] futureArray = new CompletableFuture[2];
        futureArray[0] = CompletableFuture.supplyAsync(() -> {
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody1)
                    .when().post(PATH)
                    .then().log().all().extract();
            return response.statusCode();
        });

        futureArray[1] = CompletableFuture.supplyAsync(() -> {
            ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(requestBody2)
                    .when().post(PATH)
                    .then().log().all().extract();
            return response.statusCode();
        });

        CompletableFuture.allOf(futureArray).join();

        // then
        List<Integer> failCnt = Arrays.stream(futureArray)
                .map(CompletableFuture::join)
                .filter(statusCode -> statusCode != HttpStatus.OK.value())
                .toList();

        // then
        assertThat(failCnt).hasSize(3);
    }

    @Test
    @DisplayName("이미 예약된 좌석 예약 신청 예외처리")
    void alreadyReservedSeat() {
        // given
        String requestBody = """
                {
                    "concertSeatIdList": [6],
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }
}

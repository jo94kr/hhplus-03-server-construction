package io.hhplus.server_construction.interfaces.concert;

import io.hhplus.server_construction.IntegratedTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Sql(scripts = {"classpath:/db/create_concert.sql", "classpath:/db/create_waiting.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ConcertControllerIntegratedTest extends IntegratedTest {

    private final static String PATH = "/concerts";

    @Test
    @DisplayName("만료된 토큰으로 콘서트 조회")
    void findConcertByExpiredToken() {
        // given
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("page", 0);
        requestParams.put("size", 5);

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_1");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .queryParams(requestParams)
                .when().get(PATH)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
    
    @Test
    @DisplayName("콘서트 목록을 조회한다.")
    void findConcertList() {
        // given
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("page", 0);
        requestParams.put("size", 5);

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_2");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .queryParams(requestParams)
                .when().get(PATH)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("콘서트 일정 목록을 조회한다.")
    void findConcertSchedule() {
        // given
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_2");

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("startDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        requestParams.put("endDate", LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .queryParams(requestParams)
                .when().get(PATH + "/" + 1L + "/schedules")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예약 가능한 콘서트 좌석 목록을 조회한다.")
    void findAvailableConcertSeat() {
        // given
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_2");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .when().get(PATH + "/" + 1L + "/schedules/" + 1L + "/seats")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

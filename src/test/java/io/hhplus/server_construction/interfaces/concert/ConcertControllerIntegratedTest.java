package io.hhplus.server_construction.interfaces.concert;

import io.hhplus.server_construction.IntegratedTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ConcertControllerIntegratedTest extends IntegratedTest {

    private final static String PATH = "/concerts";

    @Test
    @DisplayName("콘서트 목록을 조회한다.")
    void findConcertList() {
        // given
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("page", 0);
        requestParams.put("size", 1);

        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN");

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
        headers.put("Authorization", "DUMMY_TOKEN");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .when().get(PATH + "/" + 1L + "/schedules")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("예약 가능한 콘서트 좌석 목록을 조회한다.")
    void findConcertSeat() {
        // given
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN");

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

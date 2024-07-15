package io.hhplus.server_construction.interfaces.waiting;

import io.hhplus.server_construction.IntegratedTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"classpath:/db/create_waiting.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class WaitingIntegratedTest extends IntegratedTest {

    private static final String PATH = "/waiting";

    @Test
    @DisplayName("토큰 헤더가 없을 경우 대기열 토큰을 발급 받는다.")
    void getWaitingToken() {
        // given
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get(PATH + "/check")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("token")).isNotEmpty();
    }

    @Test
    @DisplayName("토큰 헤더가 있을 경우 대기열 정보를 조회한다.")
    void getWaitingInfo() {
        // given
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_1");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .when().get(PATH + "/check")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

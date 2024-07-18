package io.hhplus.server_construction.interfaces.payment;

import io.hhplus.server_construction.IntegratedTest;
import io.hhplus.server_construction.interfaces.controller.payment.dto.PaymentDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {
        "classpath:/db/create_waiting.sql",
        "classpath:/db/create_concert.sql",
        "classpath:/db/create_user.sql",
        "classpath:/db/create_reservation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PaymentIntegratedTest extends IntegratedTest {

    private final static String PATH = "/payment";

    @Test
    @DisplayName("예약된 콘서트 결제")
    void payment() {
        // given
        PaymentDto.Request request = new PaymentDto.Request(1L, 1L);
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_2");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(PATH)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

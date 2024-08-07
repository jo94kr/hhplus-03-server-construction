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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {
        "classpath:/db/create_waiting.sql",
        "classpath:/db/create_concert.sql",
        "classpath:/db/create_user.sql",
        "classpath:/db/create_reservation.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PaymentIntegratedTest extends IntegratedTest {

    private final static String PATH = "/payment";

    @Test
    @DisplayName("만료된 토큰으로 예약된 콘서트 결제")
    void paymentByExpiredToken() {
        // given
        PaymentDto.Request request = new PaymentDto.Request(1L, 1L);
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_1");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(PATH)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("예약된 콘서트 결제")
    void payment() {
        // given
        PaymentDto.Request request = new PaymentDto.Request(1L, 1L);
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "0e930348-14bf-4ea3-85a3-70b5cc164b46");

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
    
    @Test
    @DisplayName("동일한 사용자가 동시에 결제 요청")
    void paymentAtTheSameTime() {
        // given
        PaymentDto.Request request = new PaymentDto.Request(1L, 1L);
        Map<String, Object> headers = new HashMap<>();
        headers.put("Authorization", "DUMMY_TOKEN_2");
        
        // when
        int cnt = 5;
        CompletableFuture<Integer>[] futureArray = new CompletableFuture[cnt];
        for (int i = 0; i < cnt; i++) {
            futureArray[i] = CompletableFuture.supplyAsync(() -> {
                ExtractableResponse<Response> response = RestAssured
                        .given().log().all()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(request)
                        .when().post(PATH)
                        .then().log().all().extract();
                return response.statusCode();
            });
        }
        CompletableFuture.allOf(futureArray).join();

        List<Integer> failCnt = Arrays.stream(futureArray)
                .map(CompletableFuture::join)
                .filter(statusCode -> statusCode != HttpStatus.OK.value())
                .toList();

        // then
        assertThat(failCnt).hasSize(4);
    }
}

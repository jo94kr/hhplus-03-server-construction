package io.hhplus.server_construction.interfaces.user;

import io.hhplus.server_construction.IntegratedTest;
import io.hhplus.server_construction.application.user.facade.UserFacade;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.interfaces.controller.user.UserController;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:/db/create_user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerIntegratedTest extends IntegratedTest {

    private static final String PATH = "/users";

    @Test
    @DisplayName("사용자 잔액을 조회한다.")
    void amount() {
        // given
        Long userId = 1L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get(PATH + "/" + userId + "/amount")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("사용자의 잔액을 충전한다.")
    void charge() {
        // given
        Long userId = 1L;
        String requestBody = """
                { "amount" :1000 }
                """;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().patch(PATH + "/" + userId + "/charge")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

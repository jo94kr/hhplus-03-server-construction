package io.hhplus.server_construction;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegratedTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setPort() {
        RestAssured.port = port;
    }
}

package io.hhplus.server_construction.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    /**
     * Swagger 화면 설정
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // Info 설정
        Info info = new Info()
                .title("콘서트 예약 서비스")
                .version("V0.1")
                .description("""
                        - 유저 토큰 발급 API
                        - 예약 가능 날짜 / 좌석 API
                        - 좌석 예약 요청 API
                        - 잔액 충전 / 조회 API
                        - 결제 API
                        """);

        return new OpenAPI()
                .info(info);
    }
}

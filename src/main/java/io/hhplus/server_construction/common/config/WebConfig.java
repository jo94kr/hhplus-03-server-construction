package io.hhplus.server_construction.common.config;

import io.hhplus.server_construction.common.interceptor.WaitingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final WaitingInterceptor waitingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(waitingInterceptor)
                .addPathPatterns("/concerts/**", "/reservations/**", "/payment/**")
                .order(-1);
    }
}

package io.hhplus.server_construction.common.interceptor;

import io.hhplus.server_construction.common.interceptor.query.QueryCounter;
import io.hhplus.server_construction.common.log.ResponseInfoLogData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;


import static org.springframework.http.HttpMethod.OPTIONS;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogInterceptor implements HandlerInterceptor {

    private static final int QUERY_COUNT_WARNING_STANDARD = 5;
    private static final int TOTAL_TIME_WARNING_STANDARD_MS = 2500;

    private final QueryCounter queryCounter;
    private final long startTimeMillis = System.currentTimeMillis();

    private static final String[] LOG_DENYLIST = {
            "/"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isPreflightRequest(request)) {
            return true;
        }
        if (isDenyListedUri(request.getRequestURI())) {
            return true;
        }
        return true;
    }

    public boolean isDenyListedUri(String requestUri) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        for (String pattern : LOG_DENYLIST) {
            if (antPathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        if (isPreflightRequest(request) || isDenyListedUri(request.getRequestURI())) {
            return;
        }
        long totalTime = System.currentTimeMillis() - startTimeMillis;
        ResponseInfoLogData responseInfoLogData = new ResponseInfoLogData(MDC.get("logId"), response);
        responseInfoLogData.put("Query Count", queryCounter.count());
        responseInfoLogData.put("Total Time", totalTime + "ms");
        log.info("{}", responseInfoLogData);
        logWarning(totalTime);
    }

    private void logWarning(long totalTime) {
        String logId = MDC.get("logId");
        if (queryCounter.count() >= QUERY_COUNT_WARNING_STANDARD) {
            log.warn("[{}] : 쿼리가 {}번 이상 실행되었습니다. (총 {}번)",
                    logId,
                    QUERY_COUNT_WARNING_STANDARD,
                    queryCounter.count()
            );
        }
        if (totalTime >= TOTAL_TIME_WARNING_STANDARD_MS) {
            log.warn("[{}] : 요청을 처리하는데 {}ms 이상 소요되었습니다. (총 {}ms)",
                    logId,
                    TOTAL_TIME_WARNING_STANDARD_MS,
                    totalTime
            );
        }
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return OPTIONS.matches(request.getMethod());
    }
}

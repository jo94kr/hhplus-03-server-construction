package io.hhplus.server_construction.common.interceptor.query;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class QueryCounter {

    private int count;

    public void increase() {
        count++;
    }

    public int count() {
        return count;
    }
}

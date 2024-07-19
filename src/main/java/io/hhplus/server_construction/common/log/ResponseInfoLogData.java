package io.hhplus.server_construction.common.log;

import jakarta.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseInfoLogData {

    private final Map<String, Object> params = new LinkedHashMap<>();

    public ResponseInfoLogData(String id, HttpServletResponse response) {
        params.put("id", id);
        params.put("status", response.getStatus());
    }

    public void put(String key, Object value) {
        params.put(key, value);
    }

    @Override
    public String toString() {
        return params.toString();
    }
}

package io.hhplus.server_construction.common.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpMessage {
    private final String httpMethod;
    private final String requestUri;
    private final String httpStatus;
    private final String clientIp;
    private final String processedTime;
    private final String headers;
    private final String requestParam;
    private final String requestBody;
    private final String responseBody;

    private HttpMessage(String httpMethod, String requestUri, String httpStatus, String clientIp, String processedTime, String headers, String requestParam, String requestBody, String responseBody) {
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.httpStatus = httpStatus;
        this.clientIp = clientIp;
        this.processedTime = processedTime;
        this.headers = headers;
        this.requestParam = requestParam;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
    }

    public static HttpMessage create(ContentCachingRequestWrapper requestWrapper,
                                     ContentCachingResponseWrapper responseWrapper,
                                     long processedTime) {
        return new HttpMessage(requestWrapper.getMethod(),
                requestWrapper.getRequestURI(),
                String.valueOf(responseWrapper.getStatus()),
                requestWrapper.getRemoteAddr(),
                String.valueOf(processedTime),
                getHeader(requestWrapper).toString(),
                paramsToString(requestWrapper.getParameterMap()),
                getRequestBody(requestWrapper),
                getResponseBody(responseWrapper));
    }

    private static Map<String, String> getHeader(HttpServletRequest request) {
        Map<String, String> requestHeaders = new HashMap<>();
        Enumeration<String> requestHeaderNames = request.getHeaderNames();
        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            String headerValue = request.getHeader(headerName);
            requestHeaders.put(headerName, headerValue);
        }

        return requestHeaders;
    }

    private static String paramsToString(Map<String, String[]> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            builder.append(entry.getKey()).append("=").append(String.join(",", entry.getValue())).append("&");
        }
        return !builder.isEmpty() ? builder.substring(0, builder.length() - 1) : "";
    }

    private static String getRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return "";
                }
            }
        }
        return "";
    }

    private static String getResponseBody(HttpServletResponse response) {
        String payload = null;
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);

        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    payload = new String(buf, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    payload = "";
                }
            }
        }
        return payload != null ? payload : "";
    }

    public String toLogDate() {
        return String.format("""
                        \n[Request] %s %s (%s ms)
                        Status: %s
                        Client Ip: %s
                        Headers: %s
                        Request Param: %s
                        Request Body: %s
                        Response Body: %s
                        """,
                httpMethod,
                requestUri,
                processedTime,
                httpStatus,
                clientIp,
                headers,
                requestParam,
                requestBody,
                responseBody
        );
    }
}

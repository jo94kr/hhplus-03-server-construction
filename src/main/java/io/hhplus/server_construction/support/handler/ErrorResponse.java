package io.hhplus.server_construction.support.handler;

public record ErrorResponse(
        String code,
        String message
) {
}

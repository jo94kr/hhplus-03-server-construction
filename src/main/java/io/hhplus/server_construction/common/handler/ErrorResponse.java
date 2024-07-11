package io.hhplus.server_construction.common.handler;

public record ErrorResponse(
        String code,
        String message
) {
}

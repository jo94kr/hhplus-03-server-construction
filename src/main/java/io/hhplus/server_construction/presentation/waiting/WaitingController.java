package io.hhplus.server_construction.presentation.waiting;

import io.hhplus.server_construction.application.waiting.facade.WaitingFacade;
import io.hhplus.server_construction.presentation.waiting.dto.CheckWaitingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/waiting")
@Tag(name = "/waiting", description = "대기열")
public class WaitingController {

    private final WaitingFacade waitingFacade;

    @Operation(summary = "대기열 토큰 발급/조회")
    @GetMapping("/check")
    public ResponseEntity<CheckWaitingDto.Response> check(@Schema(description = "대기열 토큰")
                                                          @RequestHeader(required = false, name = "token") String token) {
        return ResponseEntity.ok(CheckWaitingDto.Response.from(waitingFacade.checkToken(token)));
    }
}


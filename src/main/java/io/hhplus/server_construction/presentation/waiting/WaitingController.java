package io.hhplus.server_construction.presentation.waiting;

import io.hhplus.server_construction.application.waiting.facade.WaitingFacade;
import io.hhplus.server_construction.presentation.waiting.dto.CheckWaitingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/waiting")
public class WaitingController {

    private final WaitingFacade waitingFacade;

    @GetMapping("/check")
    public ResponseEntity<CheckWaitingDto.Response> check(@RequestHeader(required = false, name = "token") String token) {
        return ResponseEntity.ok(CheckWaitingDto.Response.from(waitingFacade.checkToken(token)));
    }
}


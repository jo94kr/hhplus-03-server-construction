package io.hhplus.server_construction.presentation.waiting;

import io.hhplus.server_construction.presentation.waiting.dto.CheckWaitingDto;
import io.hhplus.server_construction.domain.waiting.WaitingEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/waiting")
public class WaitingController {

    @GetMapping("/check")
    public ResponseEntity<CheckWaitingDto.Response> check() {
        return ResponseEntity.ok(new CheckWaitingDto.Response("DUMMY_TOKEN", 10, WaitingEnums.WAITING));
    }
}

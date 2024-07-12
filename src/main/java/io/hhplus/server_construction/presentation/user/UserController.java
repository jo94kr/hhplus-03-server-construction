package io.hhplus.server_construction.presentation.user;

import io.hhplus.server_construction.application.user.facade.UserFacade;
import io.hhplus.server_construction.presentation.user.dto.AmountDto;
import io.hhplus.server_construction.presentation.user.dto.ChargeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/{userId}/amount")
    public ResponseEntity<AmountDto.Response> amount(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(new AmountDto.Response(userFacade.findUserById(userId).getAmount()));
    }

    @PatchMapping("/{userId}/charge")
    public ResponseEntity<ChargeDto.Response> charge(@PathVariable("userId") Long userId,
                                                     @RequestBody ChargeDto.Request request) {
        return ResponseEntity.ok(new ChargeDto.Response(BigDecimal.valueOf(200L)));
    }
}

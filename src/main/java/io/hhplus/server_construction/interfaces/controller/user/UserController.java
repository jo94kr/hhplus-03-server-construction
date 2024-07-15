package io.hhplus.server_construction.interfaces.controller.user;

import io.hhplus.server_construction.application.user.facade.UserFacade;
import io.hhplus.server_construction.interfaces.controller.user.dto.AmountDto;
import io.hhplus.server_construction.interfaces.controller.user.dto.ChargeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "/users", description = "사용자")
public class UserController {

    private final UserFacade userFacade;

    @Operation(summary = "사용자 잔액 조회")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = AmountDto.Response.class)))
    @GetMapping("/{userId}/amount")
    public ResponseEntity<AmountDto.Response> amount(@Schema(description = "사용자 PK")
                                                     @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(new AmountDto.Response(userFacade.findUserById(userId).getAmount()));
    }

    @Operation(summary = "사용자 잔액 충전")
    @ApiResponse(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChargeDto.Response.class)))
    @PatchMapping("/{userId}/charge")
    public ResponseEntity<ChargeDto.Response> charge(@Schema(description = "사용자 PK")
                                                     @PathVariable("userId") Long userId,
                                                     @RequestBody ChargeDto.Request request) {
        return ResponseEntity.ok(new ChargeDto.Response(userFacade.charge(userId, request.amount()).getAmount()));
    }
}

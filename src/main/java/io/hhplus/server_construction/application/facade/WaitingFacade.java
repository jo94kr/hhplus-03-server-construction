package io.hhplus.server_construction.application.facade;

import io.hhplus.server_construction.application.dto.CheckTokenResult;
import io.hhplus.server_construction.domain.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class WaitingFacade {

    private final WaitingService waitingService;

    @Transactional(rollbackFor = {Exception.class})
    public CheckTokenResult checkToken(String token) {

        return CheckTokenResult.create(null, null, null, null, null);
    }
}

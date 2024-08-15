package io.hhplus.server_construction.application.data_platform.facade;

import io.hhplus.server_construction.domain.data_platform.service.DataPlatformService;
import io.hhplus.server_construction.domain.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataPlatformFacade {

    private final DataPlatformService dataPlatformService;

    public void sendReservationInfo(Reservation reservation) throws InterruptedException {
        dataPlatformService.sendReservationInfo(reservation);
    }
}

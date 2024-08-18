package io.hhplus.server_construction.domain.data_platform.service;

import io.hhplus.server_construction.domain.reservation.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataPlatformService {

    public void sendReservationInfo(Reservation reservation) throws InterruptedException {
        log.info("Data Send Start");
        log.info(reservation.toString());
        Thread.sleep(3000);

        log.info("Data Send End");
    }
}

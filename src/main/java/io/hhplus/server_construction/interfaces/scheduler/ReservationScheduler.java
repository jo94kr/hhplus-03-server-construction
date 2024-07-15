package io.hhplus.server_construction.interfaces.scheduler;


import io.hhplus.server_construction.application.reservation.facade.ReservationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationFacade reservationFacade;

    @Scheduled(fixedRate = 10000)
    public void run() {
        reservationFacade.temporaryReservationSeatProcess();
    }
}

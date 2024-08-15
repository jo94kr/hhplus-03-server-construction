package io.hhplus.server_construction.infra.reservation;

import io.hhplus.server_construction.domain.reservation.event.ReservationEventPublisher;
import io.hhplus.server_construction.domain.reservation.event.ReservationInfoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventPublisherImpl implements ReservationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void sendDataPlatform(ReservationInfoEvent reservationInfoEvent) {
        applicationEventPublisher.publishEvent(reservationInfoEvent);
    }
}

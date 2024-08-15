package io.hhplus.server_construction.interfaces.consumer;

import io.hhplus.server_construction.application.data_platform.facade.DataPlatformFacade;
import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.service.OutboxService;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.support.kafka.KafkaConstants;
import io.hhplus.server_construction.support.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationMessageConsumer {

    private final OutboxService outboxService;
    private final DataPlatformFacade dataPlatformFacade;

    @KafkaListener(topics = KafkaConstants.RESERVATION_TOPIC, groupId = "reservation-outbox")
    public void checkOutbox(ConsumerRecord<String, String> record) {
        Outbox outbox = outboxService.findById(record.key());
        outboxService.save(outbox.published());
    }

    @KafkaListener(topics = KafkaConstants.RESERVATION_TOPIC, groupId = "reservation-process")
    public void reserved(ConsumerRecord<String, String> record) throws InterruptedException {
        Reservation reservation = JsonUtil.stringToObject(record.value(), Reservation.class);
        dataPlatformFacade.sendReservationInfo(reservation);
    }
}

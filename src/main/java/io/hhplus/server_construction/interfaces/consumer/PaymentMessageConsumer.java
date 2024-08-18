package io.hhplus.server_construction.interfaces.consumer;

import io.hhplus.server_construction.application.waiting.facade.WaitingFacade;
import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.service.OutboxService;
import io.hhplus.server_construction.infra.kafka.KafkaConstants;
import io.hhplus.server_construction.support.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMessageConsumer {

    private final OutboxService outboxService;
    private final WaitingFacade waitingFacade;

    @KafkaListener(topics = KafkaConstants.PAYMENT_TOPIC, groupId = "payment-outbox")
    public void checkOutbox(ConsumerRecord<String, String> record) {
        log.info("[payment-outbox] key: {}", record.key());
        Outbox outbox = outboxService.findById(record.key());
        outboxService.save(outbox.published());
    }

    @KafkaListener(topics = KafkaConstants.PAYMENT_TOPIC, groupId = "payment-process")
    public void tokenExpired(ConsumerRecord<String, String> record) {
        log.info("[payment-process] value: {}", record.value());
        String token = JsonUtil.stringToObject(record.value(), String.class);
        waitingFacade.expiredToken(token);
    }
}

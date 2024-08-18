package io.hhplus.server_construction.application.outbox.facade;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.service.OutboxService;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.infra.kafka.KafkaConstants;
import io.hhplus.server_construction.infra.kafka.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxFacade {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void retryOutboxMessage(LocalDateTime targetDatetime) {
        List<Outbox> outboxList = outboxService.findAllByStatusTargetDatetime(OutboxStatus.INIT, targetDatetime);

        if (outboxList.isEmpty()) {
            return;
        }

        for (Outbox outbox : outboxList) {
            if (outbox.getCnt() >= 3) {
                outboxService.save(outbox.failed());
                continue;
            }

            String topic = switch (outbox.getMessageType()) {
                case RESERVATION -> KafkaConstants.RESERVATION_TOPIC;
                case PAYMENT -> KafkaConstants.PAYMENT_TOPIC;
            };

            kafkaProducer.send(topic, outbox.getId(), outbox.getMessage());
            outboxService.save(outbox.incrementCnt());
        }
    }
}

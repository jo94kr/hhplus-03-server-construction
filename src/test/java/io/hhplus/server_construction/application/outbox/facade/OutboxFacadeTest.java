package io.hhplus.server_construction.application.outbox.facade;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.service.OutboxService;
import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.support.kafka.KafkaConstants;
import io.hhplus.server_construction.support.kafka.KafkaProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OutboxFacadeTest {

    @InjectMocks
    OutboxFacade outboxFacade;

    @Mock
    OutboxService outboxService;

    @Mock
    KafkaProducer kafkaProducer;

    @Test
    @DisplayName("재시도 횟수가 3회 이상인 outbox 실패 처리")
    void retriesMoreThan3() {
        // given
        List<Outbox> outboxList = List.of(new Outbox("test", MessageType.RESERVATION, "TEST", OutboxStatus.INIT, 4));
        LocalDateTime now = LocalDateTime.now();

        // when
        when(outboxService.findAllByStatusTargetDatetime(OutboxStatus.INIT, now))
                .thenReturn(outboxList);

        // then
        outboxFacade.retryOutboxMessage(now);
        assertThat(outboxList.get(0).getStatus()).isEqualTo(OutboxStatus.FAIL);
    }

    @Test
    @DisplayName("outbox 정상 재발행")
    void retrySuccess() {
        // given
        List<Outbox> outboxList = List.of(Outbox.init(MessageType.RESERVATION, "TEST"));
        LocalDateTime now = LocalDateTime.now();

        // when
        when(outboxService.findAllByStatusTargetDatetime(OutboxStatus.INIT, now))
                .thenReturn(outboxList);

        // then
        outboxFacade.retryOutboxMessage(now);
        Outbox outbox = outboxList.get(0);
        verify(kafkaProducer).send(KafkaConstants.RESERVATION_TOPIC, outbox.getId(), outbox.getMessage());
        verify(outboxService).save(outbox);
    }
}

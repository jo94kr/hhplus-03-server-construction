package io.hhplus.server_construction.application.outbox.facade;

import io.hhplus.server_construction.domain.outbox.Outbox;
import io.hhplus.server_construction.domain.outbox.repository.OutboxRepository;
import io.hhplus.server_construction.domain.outbox.vo.MessageType;
import io.hhplus.server_construction.domain.outbox.vo.OutboxStatus;
import io.hhplus.server_construction.support.testcontainer.KafkaTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
class OutboxFacadeIntegratedTest implements KafkaTestContainer {

    @Autowired
    OutboxFacade outboxFacade;

    @Autowired
    OutboxRepository outboxRepository;

    @Test
    @DisplayName("5분 지난 초기 상태 Outbox 를 Kafka 재발행 한다.")
    void reissueOutbox() {
        // given
        Outbox outbox = new Outbox("TEST", MessageType.RESERVATION, "TEST", OutboxStatus.INIT, 0);
        outboxRepository.save(outbox);

        // when
        outboxFacade.retryOutboxMessage(LocalDateTime.now().plusMinutes(10));

        // then
        Outbox result = outboxRepository.findById(outbox.getId());
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(outboxRepository.findById(outbox.getId()).getStatus()).isEqualTo(OutboxStatus.PUBLISHED));

        assertThat(result.getCnt()).isEqualTo(1);
    }

    @Test
    @DisplayName("재시도 회수 3회 이상 Outbox 실패 처리")
    void retriesMoreThan3() {
        // given
        Outbox outbox = new Outbox("TEST", MessageType.RESERVATION, "TEST", OutboxStatus.INIT, 3);
        outboxRepository.save(outbox);

        // when
        outboxFacade.retryOutboxMessage(LocalDateTime.now().plusMinutes(10));

        // then
        Outbox result = outboxRepository.findById(outbox.getId());
        assertThat(result.getStatus()).isEqualTo(OutboxStatus.FAIL);
    }
}

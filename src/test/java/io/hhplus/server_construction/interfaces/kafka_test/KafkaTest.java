package io.hhplus.server_construction.interfaces.kafka_test;

import io.hhplus.server_construction.IntegratedTest;
import io.hhplus.server_construction.support.testcontainer.KafkaTestContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Slf4j
class KafkaTest extends IntegratedTest implements KafkaTestContainer {

    @Autowired
    private TestConsumer testConsumer;

    @Autowired
    private TestProducer testProducer;

    @Test
    @DisplayName("Kafka 연동 테스트")
    void kafkaTest() {
        // given
        int cnt = 5;
        String topic = "testTopic";
        for (int i = 0; i < cnt; i++) {
            testProducer.send(topic, "TEST MESSAGE! num:" + (i + 1));
        }

        // when
        // then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(testConsumer.getTestPayloadList())
                        .hasSize(cnt));

        log.debug("testConsumer.getTestPayloadList(): {}", testConsumer.getTestPayloadList());
        log.debug("consume cnt: {}", testConsumer.getTestPayloadList().size());
    }
}

package io.hhplus.server_construction.support.testcontainer;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public interface KafkaTestContainer {

    String DOCKER_KAFKA_IMAGE = "confluentinc/cp-kafka:latest";

    @Container
    KafkaContainer container = new KafkaContainer(DockerImageName.parse(DOCKER_KAFKA_IMAGE));

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", container::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", container::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", container::getBootstrapServers);
    }
}

package io.hhplus.server_construction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ServerConstructionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerConstructionApplication.class, args);
    }

}

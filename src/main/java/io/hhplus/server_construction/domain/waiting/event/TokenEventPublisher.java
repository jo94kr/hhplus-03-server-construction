package io.hhplus.server_construction.domain.waiting.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TokenEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public TokenEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void expire(TokenExpireEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}

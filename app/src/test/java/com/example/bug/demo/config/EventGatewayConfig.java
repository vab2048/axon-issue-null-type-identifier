package com.example.bug.demo.config;

import org.axonframework.eventhandling.gateway.DefaultEventGateway;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class EventGatewayConfig {
    private static final Logger log = LoggerFactory.getLogger(EventGatewayConfig.class);

    /**
     * Ensure that the event gateway is the one which connects to AxonServer.
     */
    @Bean
    @Primary
    public EventGateway eventGateway(EventStore eventStore) {
        log.debug("Overriding EventGateway bean.");
        return DefaultEventGateway.builder().eventBus(eventStore).build();
    }

}

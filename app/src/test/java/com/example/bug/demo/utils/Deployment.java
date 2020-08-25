package com.example.bug.demo.utils;

import lombok.Getter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("rawtypes")
public class Deployment {
    private Network network;

    // Axon Server
    private static final int AXON_HTTP_PORT = 8024;
    private static final int AXON_GRPC_PORT = 8124;
    @Getter private GenericContainer axonServer;

    // DB
    @Getter private DbContainer dbContainer;

    public Deployment() {
        // Create the new network.
        network = Network.newNetwork();

        // Configure the axon server instance.
        axonServer = new GenericContainer("axoniq/axonserver:latest")
                .withNetwork(network)
                .withExposedPorts(AXON_HTTP_PORT, AXON_GRPC_PORT)
                // Make sure dev mode is enabled so we can drop events as needed.
                .withEnv(Map.of("axoniq.axonserver.devmode.enabled", "true"))
                .waitingFor(Wait.forLogMessage(".*Started AxonServer.*", 1));

        // Configure the db
        dbContainer = new DbContainer(network);
    }

    public void start() {
        Stream.of(axonServer, dbContainer).parallel().forEach(GenericContainer::start);
    }

}

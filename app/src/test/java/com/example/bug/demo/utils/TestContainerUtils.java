package com.example.bug.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;

public class TestContainerUtils {
    private static final Logger log = LoggerFactory.getLogger(TestContainerUtils.class);

    /**
     * Given a registry and a db container, this will register
     * the properties required to create a datasource in the application context.
     * @param registry  Registry to add property values to.
     * @param db        Database container from which to get the property values.
     */
    public static void registerPostgresDb(
            DynamicPropertyRegistry registry,
            DbContainer db) {
        log.info("Dynamically registering db for application context: {}", db.getJdbcUrl());
        registry.add("spring.datasource.url", db::getJdbcUrl);
        registry.add("spring.datasource.username", db::getUsername);
        registry.add("spring.datasource.password", db::getPassword);
    }


    /**
     * Given a registry and an axon server container, this will register
     * the properties required to connect to axon server.
     * @param registry   Registry to add property values to.
     * @param axonServer Axon Server container which we want to register the properties of.
     */
    public static void registerAxonServer(
            DynamicPropertyRegistry registry,
            GenericContainer axonServer) {
        Integer mappedPort = axonServer.getMappedPort(8124);
        log.info("Dynamically registering Axon Server for application context at localhost:{}", mappedPort);
        registry.add("axon.axonserver.servers", () -> "localhost:" + mappedPort);
    }
}

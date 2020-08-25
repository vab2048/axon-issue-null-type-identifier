package com.example.bug.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Test container definition for our Postgres DB.
 */
public class DbContainer extends PostgreSQLContainer<DbContainer>  {
    // Logger
    private static final Logger log = LoggerFactory.getLogger(DbContainer.class);

    // Container version definition.
    public static final String VERSION = "SNAPSHOT";
    public static final String CONTAINER_NAME = "example-axon/example-bug-db";
    public static final String CONTAINER_IMAGE = CONTAINER_NAME + ":" + VERSION;

    // Credentials.
    public static final String POSTGRES_USER = "postgres";
    public static final String POSTGRES_PASSWORD = "password";
    public static final String POSTGRES_DB = "bug_example";

    // Ports (from perspective of container) to expose. This host will expose the container' port on a random
    // free port (by design) to avoid port collisions. This means a mapping may need to take place at runtime.
    public static final int POSTGRES_PORT = 5432;

    // Network related fields
    public static final String POSTGRES_CONTAINER_NETWORK_ALIAS = "postgres_db";
    private final Network network;

    public DbContainer(Network network) {
        super(CONTAINER_IMAGE);
        log.debug("Creating new PostgresDB testcontainer: {}", CONTAINER_IMAGE);
        this.network = network;
    }

    /**
     * Our custom configuration for the container that will be called when the container is started.
     */
    @Override
    protected void configure() {
        // Set container fields.
        withExposedPorts(POSTGRES_PORT);
        withDatabaseName(POSTGRES_DB);
        withUsername(POSTGRES_USER);
        withPassword(POSTGRES_PASSWORD);

        // Set fields for the container's environment (overriding the defaults of 'test').
        addEnv("POSTGRES_DB", POSTGRES_DB);
        addEnv("POSTGRES_USER", POSTGRES_USER);
        addEnv("POSTGRES_PASSWORD", POSTGRES_PASSWORD);

        // Link the container in our network.
        withNetwork(network);
        withNetworkAliases(POSTGRES_CONTAINER_NETWORK_ALIAS);
    }

    /**
     * Return the JDBC URL for other containers in the same docker network to use to connect to this container.
     */
    public String getNetworkJdbcUrl() {
        return "jdbc:postgresql://" + POSTGRES_CONTAINER_NETWORK_ALIAS + ":5432/" + POSTGRES_DB + ";";
    }

}

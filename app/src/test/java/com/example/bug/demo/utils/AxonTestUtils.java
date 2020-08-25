package com.example.bug.demo.utils;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility methods for dealing with axon server instances and tables for axon in
 * our db for integration tests.
 */
public class AxonTestUtils {

    /**
     * Class logger.
     */
    public static Logger log = LoggerFactory.getLogger(AxonTestUtils.class);

    /**
     * Default HTTP Client we will use to make requests to the Axon Server.
     */
    private static final HttpClient httpClient = HttpClient.newBuilder().build();

    /**
     * The default address we will assume the server is running on.
     */
    public static final String AXON_SERVER_DEFAULT_ADDRESS = "127.0.0.1";

    /**
     * The default port we will assume the server is running on.
     */
    public static final int AXON_SERVER_DEFAULT_PORT = 8024;

    @SneakyThrows
    public static void resetEventStoreOrThrow() {
        resetEventStoreOrThrow("http", AXON_SERVER_DEFAULT_ADDRESS, AXON_SERVER_DEFAULT_PORT);
    }

    public static void resetEventStoreOrThrow(String serverAddress, int serverPort) {
        resetEventStoreOrThrow("http", serverAddress, serverPort);
    }

    @SneakyThrows
    public static void resetEventStoreOrThrow(String protocol, String serverAddress, int serverPort) {
        String endpoint = "/v1/devmode/purge-events";
        String URL = protocol + "://" + serverAddress + ":" + serverPort + endpoint;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).DELETE().build();
        log.info("Issuing HTTP request to purge-events: {}", request);

        // Try to make the HTTP call.
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                String s = String.format("Received non 200 response to reset event store: %s", response);

                // Change the error string if it is a 404.
                if (response.statusCode() == 404) {
                    s = String.format("Received 404 response from Axon Server for endpoint: %s. Are you sure dev " +
                            "mode is enabled?", response);
                }
                throw new IllegalStateException(s);
            }
        }
        // If it fails because of a timeout - it is probably because no AxonServer instance is running.
        catch (ConnectException ex) {
            String s = String.format("Unable to connect to Axon Server (%s:%s). Are you sure it is running?", serverAddress, serverPort);
            throw new IllegalStateException(s, ex);
        }
    }
}

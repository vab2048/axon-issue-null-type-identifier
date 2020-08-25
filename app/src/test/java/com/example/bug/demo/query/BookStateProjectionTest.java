package com.example.bug.demo.query;


import com.example.bug.command.messages.Messages.*;
import com.example.bug.command.records.ItemId;
import com.example.bug.demo.utils.Deployment;
import com.example.bug.demo.utils.TestContainerUtils;
import com.example.bug.query.entities.BookState;
import com.example.bug.query.entities.BookStateRepository;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static com.example.bug.demo.utils.AxonTestUtils.resetEventStoreOrThrow;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class BookStateProjectionTest {
    private static final Logger log = LoggerFactory.getLogger(BookStateProjectionTest.class);

    private static final Deployment deployment = new Deployment();

    @DynamicPropertySource
    public static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        log.info("registerDynamicProperties called in {}. ", BookStateProjectionTest.class.getName());
        deployment.start();
        TestContainerUtils.registerAxonServer(registry, deployment.getAxonServer());
        TestContainerUtils.registerPostgresDb(registry, deployment.getDbContainer());
    }

    /**
     * Configuration about all of the event processors used in the application.
     */
    private final EventProcessingConfiguration eventProcessingConfiguration;
    private final EventGateway eventGateway;
    private final BookStateRepository bookStateRepository;

    @Autowired
    public BookStateProjectionTest(EventProcessingConfiguration eventProcessingConfiguration,
                                   EventGateway eventGateway,
                                   BookStateRepository bookStateRepository) {
        this.eventProcessingConfiguration = eventProcessingConfiguration;
        this.eventGateway = eventGateway;
        this.bookStateRepository = bookStateRepository;
    }

    @BeforeEach
    void beforeEach() {
        // Reset Axon Server store (using dev mode endpoint).
        resetEventStoreOrThrow("localhost", deployment.getAxonServer().getMappedPort(8024));

        // Reset the projection's TEP
        var tepOptional = eventProcessingConfiguration.eventProcessor("book-projection", TrackingEventProcessor.class);
        var tep = tepOptional.orElseThrow();
        tep.shutDown();
        tep.resetTokens();
        tep.start();
        log.info("Event Processor [{}] has been reset", tep.getName());

        // Reset the DB table.
        log.info("Deleting all entries in the book state table");
        bookStateRepository.deleteAll();
    }

    /* ****************************************************************************
     *                                 Dummy Events
     * ****************************************************************************/
    static class DummyEvents {

        public static BookWithPriceTBCCreatedEvent bookWithPriceTBCCreatedEvent() {
            return BookWithPriceTBCCreatedEvent.builder()
                    .id(ItemId.builder().id(UUID.randomUUID().toString()).build())
                    .title("Rules to Monopoly")
                    .isbn("1234")
                    .publisher("The Monopoly Guy")
                    .build();
        }

        public static BookPriceUpdatedEvent bookPriceUpdatedEvent(ItemId id) {
            return BookPriceUpdatedEvent.builder()
                    .id(id)
                    .price("100")
                    .build();
        }
    }

    /* ****************************************************************************
     *                             Projection Tests
     * ****************************************************************************/
    @Test
    void BookWithPriceTBCCreatedEvent_ShouldCreateEntity() {
        /* Given: A BookWithPriceTBCCreatedEvent. */
        var event = DummyEvents.bookWithPriceTBCCreatedEvent();

        /* When: The event occurs. */
        log.info("Publishing Event: {}", event);
        eventGateway.publish(event);

        /* Then: There should be a new Horse Race state entry in the DB with the expected values. */
        await()
                .pollInterval(Duration.ofSeconds(2))
                .atMost(30, SECONDS)
                .untilAsserted(() -> {
                    log.info("------------ Assertion Round ------------");
                    // Retrieve entities from the repository.
                    Optional<BookState> bookState = bookStateRepository.findById(event.getId().asUUID());
                    log.info("BookState: {}", bookState);

                    // Assert expected values for the event state.
                    assertThat(bookState).isPresent();
                    assertThat(bookState.get().getAggregateId()).isEqualTo(event.getId().asUUID());
                    // rest of the assertions ....
                });
    }


    @Test
    void test2() throws InterruptedException {
        log.info("In test 2");
        Thread.sleep(2_000); // Simulate test running.
    }

    @Test
    void test3() throws InterruptedException {
        log.info("In test 3");
        Thread.sleep(2_000); // Simulate test running.
    }

    @Test
    void test4() throws InterruptedException {
        log.info("In test 4");
        Thread.sleep(2_000); // Simulate test running.
    }

}

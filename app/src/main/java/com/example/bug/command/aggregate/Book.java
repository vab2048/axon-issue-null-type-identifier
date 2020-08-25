package com.example.bug.command.aggregate;

import com.example.bug.command.messages.Messages.*;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate @Slf4j
public class Book extends AbstractItem {

    private String isbn;
    private String publisher;

    /**
     * Empty constructor required by axon.
     */
    public Book() {
        super(log);
    }

    @CommandHandler
    public Book(CreateBookWithPriceTBCCommand cmd) {
        super(log);
        log.info("@CommandHandler with command: {}", cmd);
        apply(new BookWithPriceTBCCreatedEvent(cmd.getId(), cmd.getTitle(), cmd.getIsbn(), cmd.getPublisher()));
    }

    @EventSourcingHandler
    public void on(BookWithPriceTBCCreatedEvent event) {
        log.info("@EventSourcingHandler with event: {}", event);
        this.id = event.getId();
        this.title = event.getTitle();
        this.isbn = event.getIsbn();
        this.publisher = event.getPublisher();
    }

}

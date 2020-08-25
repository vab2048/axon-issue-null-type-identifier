package com.example.bug.command.aggregate;

import com.example.bug.command.messages.Messages.*;
import com.example.bug.command.records.ItemId;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.slf4j.Logger;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * Class hierarchy example shamelessly taken from here: https://stackoverflow.com/a/46777977/5108875
 */
public abstract class AbstractItem {
    // Logger will be passed in by concrete class in empty constructor.
    protected Logger log;

    // Fields all AbstractItems have...
    @AggregateIdentifier  protected ItemId id;
    protected String title;
    protected String price;

    protected AbstractItem(Logger log) {
        this.log = log;
    }


    @CommandHandler
    public void handle(UpdatePriceOfBookCommand cmd) {
        log.info("@CommandHandler with command: {}", cmd);
        apply(new BookPriceUpdatedEvent(cmd.getId(), cmd.getPrice()));
    }

    @EventSourcingHandler
    public void on(BookPriceUpdatedEvent event) {
        log.info("@EventSourcingHandler with event: {}", event);
        this.price = event.getPrice();
    }

}

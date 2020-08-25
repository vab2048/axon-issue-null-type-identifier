package com.example.bug.query;

import com.example.bug.command.messages.Messages.*;
import com.example.bug.query.entities.BookState;
import com.example.bug.query.entities.BookStateRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("book-projection")
public class BookProjection {
    private static final Logger log = LoggerFactory.getLogger(BookProjection.class);
    private final BookStateRepository bookStateRepository;

    public BookProjection(BookStateRepository bookStateRepository) {
        this.bookStateRepository = bookStateRepository;
    }

    @EventHandler
    public void on(BookWithPriceTBCCreatedEvent event) {
        log.info("Projecting: {}", event);

        var book = new BookState();
        book.setAggregateId(event.getId().asUUID());
        book.setTitle(event.getTitle());
        book.setIsbn(event.getIsbn());
        book.setPublisher(event.getPublisher());

        bookStateRepository.save(book);
    }

    @EventHandler
    public void on(BookPriceUpdatedEvent event) {
        log.info("Projecting: {}", event);

        var bookState = bookStateRepository.getOne(event.getId().asUUID());
        bookState.setPrice(event.getPrice());
        bookStateRepository.save(bookState);
    }
}

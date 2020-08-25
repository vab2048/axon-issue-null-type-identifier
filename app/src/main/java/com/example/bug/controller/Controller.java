package com.example.bug.controller;

import com.example.bug.command.aggregate.Book;
import com.example.bug.command.messages.Messages.*;
import com.example.bug.command.records.ItemId;
import com.example.bug.query.entities.BookState;
import com.example.bug.query.messages.Messages.*;
import com.example.bug.query.responses.GetAllBooksQueryResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class Controller {
    // Unique part of the "random book" that is generated each time.
    private static int RANDOM_BOOK_COUNT = 0;

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public Controller(CommandGateway commandGateway,
                      QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping("/newRandomBookWithPriceTBC")
    public Object newRandomBook() {
        // Increment the count.
        RANDOM_BOOK_COUNT++;

        // Create the command.
        var cmd = new CreateBookWithPriceTBCCommand(
                ItemId.builder().id(UUID.randomUUID().toString()).build(),
                String.valueOf(RANDOM_BOOK_COUNT),
                String.valueOf(RANDOM_BOOK_COUNT),
                String.valueOf(RANDOM_BOOK_COUNT)
        );

        // Issue the command.
        return commandGateway.sendAndWait(cmd);
    }

    @GetMapping("/allBooks")
    public CompletableFuture<GetAllBooksQueryResponse> allBooks() {
        var query = new GetAllBooksQuery();
        return queryGateway.query(query, ResponseTypes.instanceOf(GetAllBooksQueryResponse.class));
    }

}

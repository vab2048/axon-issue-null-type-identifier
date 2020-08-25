package com.example.bug.query;


import com.example.bug.query.entities.BookState;
import com.example.bug.query.entities.BookStateRepository;
import com.example.bug.query.messages.Messages.*;
import com.example.bug.query.responses.GetAllBooksQueryResponse;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookQueryHandlers {

    private final BookStateRepository repository;


    @Autowired
    public BookQueryHandlers(BookStateRepository repository) {
        this.repository = repository;
    }


    @QueryHandler
    public GetAllBooksQueryResponse handle(GetAllBooksQuery query) {
        return new GetAllBooksQueryResponse(repository.findAll());
    }

}

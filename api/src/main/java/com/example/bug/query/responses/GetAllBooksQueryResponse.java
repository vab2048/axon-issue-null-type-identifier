package com.example.bug.query.responses;

import com.example.bug.query.entities.BookState;
import lombok.*;

import java.util.List;

@Value @Builder @AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Unfortunately is needed to play nice with Jackson
public class GetAllBooksQueryResponse {

    List<BookState> bookStates;

}

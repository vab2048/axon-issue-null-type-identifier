package com.example.bug.command.messages;

import com.example.bug.command.records.ItemId;
import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class Messages {

    /**
     * Create a book whose price is "to be confirmed" (TBC).
     */
    @Value @Builder @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Unfortunately is needed to play nice with Jackson
    public static class CreateBookWithPriceTBCCommand {
        @TargetAggregateIdentifier  ItemId id;
        String title;
        String isbn;
        String publisher;
    }

    @Value @Builder @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Unfortunately is needed to play nice with Jackson
    public static class BookWithPriceTBCCreatedEvent {
        ItemId id;
        String title;
        String isbn;
        String publisher;
    }

    /**
     * Update a book's price.
     */
    @Value @Builder @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Unfortunately is needed to play nice with Jackson
    public static class UpdatePriceOfBookCommand {
        @TargetAggregateIdentifier ItemId id;
        String price;
    }

    @Value @Builder @AllArgsConstructor
    @NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Unfortunately is needed to play nice with Jackson
    public static class BookPriceUpdatedEvent {
        ItemId id;
        String price;
    }

}

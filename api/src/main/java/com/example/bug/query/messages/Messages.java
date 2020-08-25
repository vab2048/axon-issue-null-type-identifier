package com.example.bug.query.messages;

import lombok.*;

public class Messages {

    @Value
    public static class GetAllBooksQuery {
        // Avoid jackson issues with serializing/deserializing objects with no fields by just
        // making one field.
        String id = "ALL";
    }

}

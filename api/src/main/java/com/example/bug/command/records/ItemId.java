package com.example.bug.command.records;

import lombok.*;

import java.util.UUID;

/**
 * Wrapper over a UUID.
 */
@Value @Builder @AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE) // Unfortunately is needed to play nice with Jackson
public class ItemId {

    String id;

    /**
     * Private constructor which simply assigns the UUID as the ID.
     */
    public ItemId(UUID uuid) {
        this.id = uuid.toString();
    }

    public UUID asUUID() {
        return UUID.fromString(id);
    }

    @Override
    public String toString() {
        return "ItemId(" + id + ")";
    }

}

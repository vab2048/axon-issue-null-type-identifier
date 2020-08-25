package com.example.bug.query.entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookStateRepository extends JpaRepository<BookState, UUID> {
}

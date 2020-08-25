package com.example.bug.query.entities;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Data @NoArgsConstructor
@Entity
@Table(name = "book")
public class BookState implements Serializable {

    @Id
    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private String price;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "publisher")
    private String publisher;

}

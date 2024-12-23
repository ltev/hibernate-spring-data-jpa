package com.ltev.bookdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Book implements LongIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String publisher;
    private String isbn;

    @ManyToOne              // EAGER
    private Author author;

    public Book(String title, String publisher, String isbn) {
        this(title, publisher, isbn, null);
    }

    public Book(String title, String publisher, String isbn, Author author) {
        this.title = title;
        this.publisher = publisher;
        this.isbn = isbn;
        this.author = author;
    }
}

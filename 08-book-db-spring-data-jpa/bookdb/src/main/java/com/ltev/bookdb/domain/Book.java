package com.ltev.bookdb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@Entity
@NoArgsConstructor
@Getter
@Setter
@NamedQuery(name = "findAllByIsbnLike", query = "from Book where isbn like ?1")
public class Book implements LongIdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String publisher;
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public void setAuthor(Author author) {
        this.author = author;
        //author.addBook(this);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", isbn='" + isbn + '\'' +
                ", author=" + (author instanceof HibernateProxy ? "HibernateProxy" : author) +
                '}';
    }
}

package com.ltev.bookdb.domain.repository;

import com.ltev.bookdb.domain.Book;
import com.ltev.bookdb.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    Book BOOK;

    @BeforeEach
    void setUp() {
        BOOK = new Book("Book 1", "Author 1", "ISBN 1");
    }

    @Test
    void save() {
        bookRepository.save(BOOK);

        assertThat(BOOK.getId()).isNotNull();
    }
}
package com.ltev.bookdb.domain.repository;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    void authorIdRelationship() {
        Long nonExistingAuthorId = 1L;
        BOOK.setAuthorId(nonExistingAuthorId);

        assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(
                () -> bookRepository.save(BOOK));
    }
}
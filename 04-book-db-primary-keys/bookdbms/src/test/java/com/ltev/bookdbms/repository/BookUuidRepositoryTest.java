package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.BookUuid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class BookUuidRepositoryTest {

    @Autowired
    private BookUuidRepository bookUuidRepository;

    @Test
    void save() {
        BookUuid bookUuid = new BookUuid();
        bookUuidRepository.save(bookUuid);

        assertThat(bookUuid.getId()).isNotNull();
    }

    @Test
    void findById() {
        BookUuid bookUuid = new BookUuid();
        bookUuidRepository.save(bookUuid);
        UUID id = bookUuid.getId();

        assertThat(bookUuidRepository.findById(id)).isNotEmpty();
    }
}
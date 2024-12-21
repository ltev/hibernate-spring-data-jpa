package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.AuthorUuid;
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
class AuthorUuidRepositoryTest {

    @Autowired
    private AuthorUuidRepository authorUuidRepository;

    @Test
    void save() {
        AuthorUuid authorUuid = new AuthorUuid();
        authorUuidRepository.save(authorUuid);

        assertThat(authorUuid.getId()).isNotNull();
    }

    @Test
    void findById() {
        AuthorUuid authorUuid = new AuthorUuid();
        authorUuidRepository.save(authorUuid);
        UUID id = authorUuid.getId();

        assertThat(authorUuidRepository.findById(id)).isNotEmpty();
    }
}
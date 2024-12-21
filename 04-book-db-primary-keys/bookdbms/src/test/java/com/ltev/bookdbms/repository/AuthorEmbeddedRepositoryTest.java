package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.composite.AuthorEmbeddedId;
import com.ltev.bookdbms.domain.composite.NameId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class AuthorEmbeddedRepositoryTest {

    @Autowired
    private AuthorEmbeddedRepository authorEmbeddedRepository;

    @Test
    void save() {
        long startCount = authorEmbeddedRepository.count();
        AuthorEmbeddedId author = new AuthorEmbeddedId(new NameId("Eva", "Johnson"), "US");
        authorEmbeddedRepository.save(author);

        assertThat(authorEmbeddedRepository.count()).isEqualTo(startCount + 1);
    }

    @Test
    void findById() {
        AuthorEmbeddedId author = new AuthorEmbeddedId(new NameId("Eva", "Johnson"), "US");
        authorEmbeddedRepository.save(author);

        NameId id = new NameId("Eva", "Johnson");

        assertThat(authorEmbeddedRepository.findById(id)).isNotEmpty();
    }
}
package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.composite.AuthorCompositeId;
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
class AuthorCompositeRepositoryTest {

    @Autowired
    private AuthorCompositeRepository authorCompositeRepository;

    @Test
    void save() {
        long startCount = authorCompositeRepository.count();
        AuthorCompositeId author = new AuthorCompositeId("Dave", "Johnson", "US");
        authorCompositeRepository.save(author);

        assertThat(authorCompositeRepository.count()).isEqualTo(startCount + 1);
    }

    @Test
    void findById() {
        AuthorCompositeId author = new AuthorCompositeId("Dave", "Johnson", "US");
        authorCompositeRepository.save(author);

        NameId id = new NameId("Dave", "Johnson");

        assertThat(authorCompositeRepository.findById(id)).isNotEmpty();
    }
}
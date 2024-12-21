package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AuthorDaoImpl.class)
class AuthorDaoImplTest {

    @Autowired
    private AuthorDao authorDao;

    Author author;

    @BeforeEach
    void setUp() {
        author = new Author("Dave", "Willow");
    }

    @Test
    void save() {
        Author saved = authorDao.save(author);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findById() {
        authorDao.save(author);

        Author found = authorDao.findById(author.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(author.getId());
        assertTrue(equals(author, found));
    }

    @Test
    void update() {
        authorDao.save(author);
        author.setFirstName("Mike");
        author.setLastName("Severic");

        // update
        authorDao.save(author);

        Author found = authorDao.findById(author.getId()).get();
        System.out.println(author);
        System.out.println(found);
        assertTrue(equals(author, found));
    }

    // == PRIVATE HELPER METHODS ==

    private boolean equals(Author a1, Author a2) {
        return
                a1.getId().equals(a2.getId())
                && a1.getFirstName().equals(a2.getFirstName())
                && a1.getLastName().equals(a2.getLastName());
    }
}
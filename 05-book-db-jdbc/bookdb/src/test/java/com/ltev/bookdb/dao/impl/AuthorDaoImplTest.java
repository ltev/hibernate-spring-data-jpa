package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

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
    void count() {
        long startCount = authorDao.count();

        authorDao.save(author);         // save
        authorDao.save(author);         // update
        authorDao.save(new Author());   // save

        assertThat(authorDao.count()).isEqualTo(startCount + 2);
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
        assertTrue(equalsWithId(author, found));
    }

    @Test
    void update() {
        authorDao.save(author);
        author.setFirstName("Mike");
        author.setLastName("Severic");

        // update
        authorDao.save(author);
        Author found = authorDao.findById(author.getId()).get();

        assertTrue(equalsWithId(author, found));
    }

    @Test
    void findByFirstNameAndLastName() {
        // Save two times Author with name: Erik McGrab
        authorDao.save(new Author("Erik", "McGrab"));
        authorDao.save(new Author("Erik", "Willow"));
        authorDao.save(new Author("Erik", "McGrab"));
        authorDao.save(new Author("Ela", "McGrab"));

        Author erikMcGrab = new Author("Erik", "McGrab");
        List<Author> list = authorDao.findByFirstNameAndLastName(erikMcGrab.getFirstName(), erikMcGrab.getLastName());

        assertThat(list.size()).isEqualTo(2);
        assertTrue(equalsNoId(erikMcGrab, list.get(0)));
        assertTrue(equalsNoId(erikMcGrab, list.get(1)));
    }

    @Test
    void deleteById() {
        authorDao.save(author);
        long countAfterSave = authorDao.count();

        authorDao.deleteById(author.getId());
        assertThat(authorDao.count()).isEqualTo(countAfterSave - 1);
    }

    // == PRIVATE HELPER METHODS ==

    private boolean equalsWithId(Author a1, Author a2) {
        return  a1.getId().equals(a2.getId())
                && equalsNoId(a1, a2);
    }

    private boolean equalsNoId(Author a1, Author a2) {
        return  a1.getFirstName().equals(a2.getFirstName())
                && a1.getLastName().equals(a2.getLastName());
    }
}
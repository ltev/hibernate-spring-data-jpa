package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static com.ltev.bookdb.TestSupport.equalsNoId;
import static com.ltev.bookdb.TestSupport.equalsWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({AuthorDaoImpl.class, BookDaoImpl.class})
class AuthorDaoImplTest {

    @Autowired
    private AuthorDaoImpl authorDao;

    @Autowired
    private BookDaoImpl bookDao;

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
    void saveInBatch() {
        long count = authorDao.count();
        int rowsAffected = authorDao.saveInBatch(List.of(new Author(), author, new Author()));

        assertThat(rowsAffected).isEqualTo(3);
        assertThat(authorDao.count()).isEqualTo(count + 3);
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
    void findById_nonExisting() {
        Optional<Author> found = authorDao.findById(-1L);

        assertThat(found).isEmpty();
    }

    @Test
    void update() {
        authorDao.save(author);

        // update
        author.setFirstName("Mike");
        author.setLastName("Severic");
        authorDao.save(author);

        Author found = authorDao.findById(author.getId()).get();

        assertTrue(equalsWithId(author, found));
    }

    @Test
    void findByFirstNameAndLastName() {
        Author erikMcGrab = new Author("Erik", "McGrab");
        long countBefore = authorDao.findByFirstNameAndLastName(erikMcGrab.getFirstName(), erikMcGrab.getLastName()).size();

        // Save two times Author with name: Erik McGrab
        authorDao.save(new Author("Erik", "McGrab"));
        authorDao.save(new Author("Erik", "Willow"));
        authorDao.save(new Author("Erik", "McGrab"));
        authorDao.save(new Author("Ela", "McGrab"));

        List<Author> list = authorDao.findByFirstNameAndLastName(erikMcGrab.getFirstName(), erikMcGrab.getLastName());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(erikMcGrab, list.get(0)));
        assertTrue(equalsNoId(erikMcGrab, list.get(1)));
    }

    @Test
    void deleteById() {
        authorDao.save(author);
        long countAfterSave = authorDao.count();

        authorDao.deleteById(author.getId());

        assertThat(authorDao.count()).isEqualTo(countAfterSave - 1);
        assertThat(authorDao.findById(author.getId())).isEmpty();
    }


    @Test
    void findById_joinFetchBooks_size0() {
        authorDao.save(author);

        Author found = authorDao.findByIdJoinFetchBooks(author.getId()).get();

        assertTrue(equalsWithId(author, found));
        assertThat(found.getBooks().size()).isEqualTo(0);
    }

    @Test
    void findById_joinFetchBooks_size2() {
        authorDao.save(author);

        var book1 = new Book("How to do", "publisher 1", "2342312");
        var book2 = new Book("How not to do ", "publisher 2", "2342324");
        book1.setAuthor(author);
        book2.setAuthor(author);

        bookDao.save(book1);
        bookDao.save(book2);

        Author found = authorDao.findByIdJoinFetchBooks(author.getId()).get();

        assertTrue(equalsWithId(author, found));
        assertThat(found.getBooks().size()).isEqualTo(2);
        assertTrue(equalsWithId(book1, found.getBooks().get(0)));
        assertTrue(equalsWithId(book2, found.getBooks().get(1)));
    }

    @Test
    void findById_joinFetchBooks_nonExistingId() {
        Optional<Author> found = authorDao.findByIdJoinFetchBooks(-1L);

        assertThat(found).isEmpty();
    }
}
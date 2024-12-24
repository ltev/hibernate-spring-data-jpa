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

import static com.ltev.bookdb.TestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BookDaoImpl.class, AuthorDaoImpl.class})
class BookDaoImplTest {

    @Autowired
    private BookDaoImpl bookDao;

    @Autowired
    private AuthorDaoImpl authorDao;

    Book book;
    Author author;

    @BeforeEach
    void setUp() {
        book = new Book("Title 1", "Publisher 1", "123456789");
        author = new Author("Dave", "Willow");
    }

    @Test
    void count() {
        long startCount = bookDao.count();

        bookDao.save(book);         // save
        bookDao.save(book);         // update
        bookDao.save(new Book());   // save

        assertThat(bookDao.count()).isEqualTo(startCount + 2);
    }

    @Test
    void save() {
        Book saved = bookDao.save(book);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findById() {
        bookDao.save(book);

        Book found = bookDao.findById(book.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(book.getId());
        assertTrue(equalsWithId(book, found));
    }

    @Test
    void findById_withAuthor() {
        authorDao.save(author);

        book.setAuthor(author);
        bookDao.save(book);

        Book found = bookDao.findById(book.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(book.getId());
        assertTrue(equalsWithId(book, found));
        assertTrue(equalsWithId(book.getAuthor(), found.getAuthor()));
        assertThat(book.getAuthor().getBooks()).isNull();    // FetchType.LAZY
    }

    @Test
    void update() {
        bookDao.save(book);
        book.setTitle("New title");
        book.setIsbn("123");

        // update
        bookDao.save(book);
        Book found = bookDao.findById(book.getId()).get();

        assertTrue(equalsWithId(book, found));
    }

    @Test
    void findByTitle() {
        String TITLE = "This one 2";
        long countBefore = bookDao.findByTitle(TITLE).size();

        // Save twice
        bookDao.save(new Book(TITLE, "This publisher", "123456789"));
        bookDao.save(new Book("x", "y", "z"));
        bookDao.save(new Book("x", "y", "z"));
        bookDao.save(new Book(TITLE, "This publisher", "123456789"));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookDao.findByTitle(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
    }

    @Test
    void findByTitle_withAuthor() {
        authorDao.save(author);

        String TITLE = "This one 2";
        long countBefore = bookDao.findByTitle(TITLE).size();

        // Save twice
        bookDao.save(new Book(TITLE, "This publisher", "123456789", author));
        bookDao.save(new Book("x", "y", "z"));
        bookDao.save(new Book("x", "y", "z"));
        bookDao.save(new Book(TITLE, "This publisher", "123456789", author));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookDao.findByTitle(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
        assertTrue(equalsWithId(author, list.get(list.size() - 1).getAuthor()));
        assertTrue(equalsWithId(author, list.get(list.size() - 2).getAuthor()));
    }

    @Test
    void deleteById() {
        bookDao.save(book);
        long countAfterSave = bookDao.count();

        bookDao.deleteById(book.getId());
        assertThat(bookDao.count()).isEqualTo(countAfterSave - 1);
    }

    @Test
    void deleteById_withAuthor() {
        authorDao.save(author);
        long authorCountAfterSave = authorDao.count();

        book.setAuthor(author);
        bookDao.save(book);
        long bookCountAfterSave = bookDao.count();

        bookDao.deleteById(book.getId());

        assertThat(bookDao.count()).isEqualTo(bookCountAfterSave - 1);
        assertThat(authorDao.count()).isEqualTo(authorCountAfterSave);              // author should not be deleted
    }

    @Test
    void findByIsbnLike() {
        String startWith = "123";
        var books = List.of(
                new Book("xyz", "xyz", startWith + "543093"),
                new Book("xyz", "xyz", "000543093"),
                new Book("xyz", "xyz", startWith + "343093"),
                new Book("xyz", "xyz", startWith + "543093"),
                new Book("xyz", "xyz", "0003093"));

        for (Book b : books) {
            bookDao.save(b);
        }

        List<Book> found = bookDao.findByIsbnLike(startWith);

        assertThat(found.size()).isGreaterThanOrEqualTo(3);
        assertThat(found.stream().allMatch(b -> b.getIsbn().startsWith(startWith))).isTrue();
    }
}
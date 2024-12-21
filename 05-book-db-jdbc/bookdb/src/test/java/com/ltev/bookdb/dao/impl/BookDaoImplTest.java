package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
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
@Import(BookDaoImpl.class)
class BookDaoImplTest {

    @Autowired
    private BookDao bookDao;

    Book book;

    @BeforeEach
    void setUp() {
        book = new Book("Title 1", "Publisher 1", "123456789");
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
        // Save two times Books
        bookDao.save(new Book("This one", "This publisher", "123456789"));
        bookDao.save(new Book("x", "y", "z"));
        bookDao.save(new Book("x", "y", "z"));
        bookDao.save(new Book("This one", "This publisher", "123456789"));

        Book thisOne = new Book("This one", "This publisher", "123456789");
        List<Book> list = bookDao.findByTitle(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
    }

    @Test
    void deleteById() {
        bookDao.save(book);
        long countAfterSave = bookDao.count();

        bookDao.deleteById(book.getId());
        assertThat(bookDao.count()).isEqualTo(countAfterSave - 1);
    }

    // == PRIVATE HELPER METHODS ==

    private boolean equalsWithId(Book b1, Book b2) {
        return  b1.getId().equals(b2.getId())
                && equalsNoId(b1, b2);
    }

    private boolean equalsNoId(Book b1, Book b2) {
        return  b1.getTitle().equals(b2.getTitle())
                && b1.getPublisher().equals(b2.getPublisher())
                && b1.getIsbn().equals(b2.getIsbn());
    }
}
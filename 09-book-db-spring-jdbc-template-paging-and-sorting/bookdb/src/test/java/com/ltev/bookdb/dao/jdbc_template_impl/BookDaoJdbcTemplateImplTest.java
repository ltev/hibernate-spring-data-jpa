package com.ltev.bookdb.dao.jdbc_template_impl;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static com.ltev.bookdb.TestSupport.equalsNoId;
import static com.ltev.bookdb.TestSupport.equalsWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BookDaoJdbcTemplateImpl.class, AuthorDaoJdbcTemplateImpl.class})
class BookDaoJdbcTemplateImplTest {

    @Autowired
    private BookDaoJdbcTemplateImpl bookDao;

    @Autowired
    private AuthorDaoJdbcTemplateImpl authorDao;

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
    void findAll() {
        bookDao.save(new Book("hj hej", "p1", "123"));
        bookDao.save(new Book("hj hej 2", "p1", "123"));

        List<Book> found = bookDao.findAll();

        assertThat(found.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void findAll_page1() {
        for (int i = 0; i < 5; i++) {
            bookDao.save(new Book("" + i, "" + i, "" + i));
        }

        List<Book> found = bookDao.findAll(5, 0);

        assertThat(found.size()).isEqualTo(5);
    }

    @Test
    void findAll_page2() {
        for (int i = 0; i < 10; i++) {
            bookDao.save(new Book("" + i, "" + i, "" + i));
        }

        List<Book> found = bookDao.findAll(5, 5);

        assertThat(found.size()).isEqualTo(5);
    }

    @Test
    void findAll_pageableSortable_sortByTitleAsc() {
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            bookDao.save(new Book("findAll_pageableSortable: " + random.nextInt(0, 100), "" + i, "" + i));
        }

        List<Book> found = bookDao.findAll(
                PageRequest.of(1, 5),
                Sort.by(Sort.Order.asc("title")));

        assertThat(found.size()).isEqualTo(4);
        assertThat(found).isSortedAccordingTo(Comparator.comparing(Book::getTitle));
    }

    @Test
    void findAll_pageableSortable_sortByPublisherDescTitleAsc() {
        Random random = new Random();
        String[] publishers = {"p1", "p2"};
        String[] titles = {"t1", "t2"};

        for (int i = 0; i < 25; i++) {
            bookDao.save(new Book(titles[random.nextInt(0, 2)], publishers[random.nextInt(0, 2)], null));
        }

        List<Book> found = bookDao.findAll(
                PageRequest.of(0, 20),
                Sort.by(Sort.Order.desc("publisher"), Sort.Order.asc("title"))
        );

        assertThat(found.size()).isEqualTo(20);
        for (int i = 1; i < found.size(); i++) {
            var b1 = found.get(i - 1);
            var b2 = found.get(i);
            assertThat(b1.getPublisher()).isGreaterThanOrEqualTo(b2.getPublisher());

            if (b1.getPublisher().equals(b2.getPublisher())) {
                assertThat(b1.getTitle()).isLessThanOrEqualTo(b2.getTitle());
            }
        }
    }

    @Test
    void findAllByTitle_page2() {
        String tile = "Unique title for test: findAllByTitle_page2";
        for (int i = 0; i < 8; i++) {
            bookDao.save(new Book(tile, null, null));
        }

        List<Book> found = bookDao.findAllByTitle(tile, 5, 5);

        assertThat(found.size()).isEqualTo(3);
    }

    @Test
    void findAllByTitle_pageablePage2() {
        String tile = "Unique title for test: findAllByTitle_pageablePage2";
        for (int i = 0; i < 8; i++) {
            bookDao.save(new Book(tile, null, null));
        }

        // Pageable pageable = Pageable.ofSize(5).withPage(1);                      // page indexes start at 0
        Pageable pageable = PageRequest.of(1, 5);               // page indexes start at 0
        List<Book> found = bookDao.findAllByTitle(tile, pageable);

        assertThat(found.size()).isEqualTo(3);
    }

    @Test
    void findAllSortByTitleAsc() {
        bookDao.save(new Book("bbb", null, null));
        bookDao.save(new Book("ccc", null, null));
        bookDao.save(new Book("AAA", null, null));
        bookDao.save(new Book("aaa", null, null));

        List<Book> found = bookDao.findAllSortByTitleAsc();

        for (int i = 1; i < found.size(); i++) {
            var b1 = found.get(i - 1);
            if (b1.getTitle() == null) {
                continue;
            }
            var b2 = found.get(i);
            assertThat(b1.getTitle().toLowerCase()).isLessThanOrEqualTo(b2.getTitle().toLowerCase());
        }
    }
}
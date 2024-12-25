package com.ltev.bookdb.repository;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.ltev.bookdb.TestSupport.equalsNoId;
import static com.ltev.bookdb.TestSupport.equalsWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    Book book;
    Author author;

    @BeforeEach
    void setUp() {
        book = new Book("Title 1", "Publisher 1", "123456789");
        author = new Author("Dave", "Willow");
    }

    @Test
    void count() {
        long startCount = bookRepository.count();

        bookRepository.save(book);         // save
        bookRepository.save(book);         // update
        bookRepository.save(new Book());   // save

        assertThat(bookRepository.count()).isEqualTo(startCount + 2);
    }

    @Test
    void save() {
        Book saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findById() {
        bookRepository.save(book);

        Book found = bookRepository.findById(book.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(book.getId());
        assertTrue(equalsWithId(book, found));
        assertThat(found.getAuthor()).isNull();
    }

    @Test
    void findById_withAuthorEager() {
        authorRepository.save(author);

        book.setAuthor(author);
        bookRepository.save(book);

        Book found = bookRepository.findById(book.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(book.getId());
        assertTrue(equalsWithId(book, found));
        assertTrue(equalsWithId(book.getAuthor(), found.getAuthor()));
    }

    @Test
    void findById_withAuthorLazy() {
        authorRepository.save(author);

        book.setAuthor(author);
        bookRepository.save(book);

        Book found = bookRepository.findById(book.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertTrue(equalsWithId(book, found));
        assertThatExceptionOfType(LazyInitializationException.class).isThrownBy(() -> book.getAuthor().getId());
    }

    @Test
    void update() {
        bookRepository.save(book);
        book.setTitle("New title");
        book.setIsbn("123");

        // update
        bookRepository.save(book);
        Book found = bookRepository.findById(book.getId()).get();

        assertTrue(equalsWithId(book, found));
    }

    @Test
    void findByTitle() {
        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitle(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitle(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
    }

    @Test
    void findByTitle_withAuthor() {
        authorRepository.save(author);

        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitle(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitle(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
        assertTrue(equalsWithId(author, list.get(list.size() - 1).getAuthor()));
        assertTrue(equalsWithId(author, list.get(list.size() - 2).getAuthor()));
    }

    @Test
    void findByTitle_query() {
        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitleQuery(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitleQuery(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
    }

    @Test
    void findByTitle_withAuthor_query() {
        authorRepository.save(author);

        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitleQuery(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitleQuery(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
        assertTrue(equalsWithId(author, list.get(list.size() - 1).getAuthor()));
        assertTrue(equalsWithId(author, list.get(list.size() - 2).getAuthor()));
    }

    @Test
    void findByTitle_queryNamed() {
        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitleQueryNamed(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitleQueryNamed(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
    }

    @Test
    void findByTitle_withAuthor_queryNamed() {
        authorRepository.save(author);

        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitleQueryNamed(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitleQueryNamed(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
        assertTrue(equalsWithId(author, list.get(list.size() - 1).getAuthor()));
        assertTrue(equalsWithId(author, list.get(list.size() - 2).getAuthor()));
    }

    @Test
    void findByTitle_nativeQuery() {
        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitleNativeQuery(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789"));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitleNativeQuery(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
    }

    @Test
    void findByTitle_withAuthor_nativeQuery() {
        authorRepository.save(author);

        String TITLE = "This one 2";
        long countBefore = bookRepository.findByTitleNativeQuery(TITLE).size();

        // Save twice
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book("x", "y", "z"));
        bookRepository.save(new Book(TITLE, "This publisher", "123456789", author));

        Book thisOne = new Book(TITLE, "This publisher", "123456789");
        List<Book> list = bookRepository.findByTitleNativeQuery(thisOne.getTitle());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(thisOne, list.get(0)));
        assertTrue(equalsNoId(thisOne, list.get(1)));
        assertTrue(equalsWithId(author, list.get(list.size() - 1).getAuthor()));
        assertTrue(equalsWithId(author, list.get(list.size() - 2).getAuthor()));
    }

    @Test
    void deleteById() {
        bookRepository.save(book);
        long countAfterSave = bookRepository.count();

        bookRepository.deleteById(book.getId());
        assertThat(bookRepository.count()).isEqualTo(countAfterSave - 1);
    }

    @Test
    void deleteById_withAuthor() {
        authorRepository.save(author);
        long authorCountAfterSave = authorRepository.count();

        book.setAuthor(author);
        bookRepository.save(book);
        long bookCountAfterSave = bookRepository.count();

        bookRepository.deleteById(book.getId());

        assertThat(bookRepository.count()).isEqualTo(bookCountAfterSave - 1);
        assertThat(authorRepository.count()).isEqualTo(authorCountAfterSave);              // author should not be deleted
    }

    @Test
    void findByIsbn() {
        bookRepository.save(book);

        Book found = bookRepository.findByIsbn(book.getIsbn());

        assertTrue(equalsWithId(book, found));
    }

    @Test
    void queryByIsbn_asyncFuture() throws ExecutionException, InterruptedException {
        String isbn = "789456123abc";
        bookRepository.save(new Book("Query by isbn", "new publisher", isbn));

        Future<Book> futureBook = bookRepository.queryByIsbn(isbn);
        Book found = futureBook.get();

        assertThat(found).isNotNull();
    }
}
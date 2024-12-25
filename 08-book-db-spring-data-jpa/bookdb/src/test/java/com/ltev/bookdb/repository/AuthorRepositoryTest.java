package com.ltev.bookdb.repository;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.ltev.bookdb.TestSupport.equalsNoId;
import static com.ltev.bookdb.TestSupport.equalsWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    Author author;


    @BeforeEach
    void setUp() {
        author = new Author("Dave", "Willow");
    }

    @Test
    void count() {
        long startCount = authorRepository.count();

        authorRepository.save(author);         // save
        authorRepository.save(author);         // update
        authorRepository.save(new Author());   // save

        assertThat(authorRepository.count()).isEqualTo(startCount + 2);
    }

    @Test
    void save() {
        Author saved = authorRepository.save(author);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void saveInBatch() {
        authorRepository.save(author);
        long countBefore = authorRepository.count();

        authorRepository.saveAll(List.of(new Author(), author, new Author())).size();
        long countAfter = authorRepository.count();

        assertThat(countAfter).isEqualTo(countBefore + 2);
    }

    @Test
    void findById() {
        authorRepository.save(author);

        Author found = authorRepository.findById(author.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(author.getId());
        assertTrue(equalsWithId(author, found));
    }

    @Test
    void findById_lazyBooks() {
        authorRepository.save(author);

        var book1 = new Book("How to do", "publisher 1", "2342312");
        var book2 = new Book("How not to do ", "publisher 2", "2342324");
        book1.setAuthor(author);
        book2.setAuthor(author);

        bookRepository.save(book1);
        bookRepository.save(book2);

        Author found = authorRepository.findById(author.getId()).get();

        assertThat(found.getId()).isNotNull();
        assertThat(found.getId()).isEqualTo(author.getId());
        assertTrue(equalsWithId(author, found));
        assertThatExceptionOfType(LazyInitializationException.class).isThrownBy(() -> found.getBooks().get(0));
    }

    @Test
    void findById_nonExisting() {
        Optional<Author> found = authorRepository.findById(-1L);

        assertThat(found).isEmpty();
    }

    @Test
    void update() {
        authorRepository.save(author);

        // update
        author.setFirstName("Mike");
        author.setLastName("Severic");
        authorRepository.save(author);

        Author found = authorRepository.findById(author.getId()).get();

        assertTrue(equalsWithId(author, found));
    }

    @Test
    void findByFirstNameAndLastName() {
        Author erikMcGrab = new Author("Erik", "McGrab");
        long countBefore = authorRepository.findByFirstNameAndLastName(erikMcGrab.getFirstName(), erikMcGrab.getLastName()).size();

        // Save two times Author with name: Erik McGrab
        authorRepository.save(new Author("Erik", "McGrab"));
        authorRepository.save(new Author("Erik", "Willow"));
        authorRepository.save(new Author("Erik", "McGrab"));
        authorRepository.save(new Author("Ela", "McGrab"));

        List<Author> list = authorRepository.findByFirstNameAndLastName(erikMcGrab.getFirstName(), erikMcGrab.getLastName());

        assertThat(list.size()).isEqualTo(countBefore + 2);
        assertTrue(equalsNoId(erikMcGrab, list.get(0)));
        assertTrue(equalsNoId(erikMcGrab, list.get(1)));
    }

    @Test
    @Transactional
    void findByLastNameLike() {
        String LAST_NAME_START = "McG";

        Author erikMcGrab = new Author("Erik", "McGrab");
        long countBefore = authorRepository.findByLastNameLike(LAST_NAME_START + "%").count();

        // Save 4 times McG%
        authorRepository.save(new Author("Erik", "McGrab"));
        authorRepository.save(new Author("Dave", "McGyver"));
        authorRepository.save(new Author("Erik", "Willow"));
        authorRepository.save(new Author("Erik", "McGursky"));
        authorRepository.save(new Author("Ela", "McGrab"));

        Stream<Author> stream = authorRepository.findByLastNameLike(LAST_NAME_START + "%");

        assertThat(stream.count()).isEqualTo(countBefore + 4);
    }

    @Test
    void deleteById() {
        authorRepository.save(author);
        long countAfterSave = authorRepository.count();

        authorRepository.deleteById(author.getId());

        assertThat(authorRepository.count()).isEqualTo(countAfterSave - 1);
        assertThat(authorRepository.findById(author.getId())).isEmpty();
    }


    @Test
    void findById_joinFetchBooks_size0() {
        authorRepository.save(author);

        Author found = authorRepository.findByIdJoinFetchBooks(author.getId()).get();

        assertTrue(equalsWithId(author, found));
        assertThat(found.getBooks().size()).isEqualTo(0);
    }

    @Test
    void findById_joinFetchBooks_size2() {
        authorRepository.save(author);

        var book1 = new Book("How to do", "publisher 1", "2342312");
        var book2 = new Book("How not to do ", "publisher 2", "2342324");
        book1.setAuthor(author);
        book2.setAuthor(author);

        bookRepository.save(book1);
        bookRepository.save(book2);

        Author found = authorRepository.findByIdJoinFetchBooks(author.getId()).get();

        /*
        Using SpringBookTest - found works property and retrieves 2 Books
        Using DataJpaTest - List is empty
        - it uses normally in other test unit with the generated here id
        - solution 1: add @Transactional(propagation = Propagation.NOT_SUPPORTED)
        - solution 2: add bidirectional connection in Book.setAuthor
         */
        assertTrue(equalsWithId(author, found));
        assertThat(found.getBooks().size()).isEqualTo(2);
        assertTrue(equalsWithId(book1, found.getBooks().get(0)));
        assertTrue(equalsWithId(book2, found.getBooks().get(1)));
        assertThat(found.getBooks().get(0).getAuthor().getId()).isEqualTo(author.getId());
    }

    @Test
    void findById_joinFetchBooks_nonExistingId() {
        Optional<Author> found = authorRepository.findByIdJoinFetchBooks(-1L);

        assertThat(found).isEmpty();
    }
}
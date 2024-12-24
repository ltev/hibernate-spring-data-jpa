package com.ltev.bookdb.dao.bootstrap;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import com.ltev.bookdb.repository.AuthorRepository;
import com.ltev.bookdb.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        Author author = new Author("Dave", "Brown");

        authorRepository.save(author);

        var book1 = new Book("How to do", "publisher 1", "2342312");
        var book2 = new Book("How not to do ", "publisher 2", "2342324");
        //book1.setAuthor(author);
        //book2.setAuthor(author);

        bookRepository.save(book1);
        bookRepository.save(book2);

        Author found = authorRepository.findByIdJoinFetchBooks(author.getId()).get();
        System.out.println(found);
    }
}

package com.ltev.bookdb.bootstrap;

import com.ltev.bookdb.domain.Book;
import com.ltev.bookdb.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        bookRepository.save(new Book("Book 1", "Author 1", "Genre 1"));
    }
}
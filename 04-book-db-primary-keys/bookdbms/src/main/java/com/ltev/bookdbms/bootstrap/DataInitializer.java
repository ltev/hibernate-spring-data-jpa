package com.ltev.bookdbms.bootstrap;

import com.ltev.bookdbms.domain.AuthorUuid;
import com.ltev.bookdbms.domain.Book;
import com.ltev.bookdbms.repository.AuthorUuidRepository;
import com.ltev.bookdbms.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value = {"local", "default"})
@Configuration
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final AuthorUuidRepository authorUuidRepository;

    @Override
    public void run(String... args) throws Exception {

        // save book
        bookRepository.save(new Book("some title"));

        // save author uuid
        authorUuidRepository.save(new AuthorUuid("Dave", "Johnson"));
    }
}

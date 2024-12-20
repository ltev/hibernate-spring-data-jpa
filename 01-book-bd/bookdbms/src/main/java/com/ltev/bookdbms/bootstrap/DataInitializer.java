package com.ltev.bookdbms.bootstrap;

import com.ltev.bookdbms.domain.Book;
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

    @Override
    public void run(String... args) throws Exception {
        bookRepository.save(new Book("some title"));
    }
}

package com.ltev.bookdb.dao.bootstrap;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import com.ltev.bookdb.repository.AuthorRepository;
import com.ltev.bookdb.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
    }
}

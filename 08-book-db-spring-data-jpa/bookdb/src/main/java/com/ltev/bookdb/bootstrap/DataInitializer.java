package com.ltev.bookdb.bootstrap;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BookDao bookDao;

    @Override
    public void run(String... args) throws Exception {
        Book book2 = new Book("Book 2", "Author 2", "Genre 2");
        bookDao.save(book2);
        System.out.println(book2);
    }
}
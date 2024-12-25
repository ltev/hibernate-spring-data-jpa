package com.ltev.bookdb.repository;

import com.ltev.bookdb.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitle(String title);

    @Nullable
    Book findByIsbn(@Nullable String isbn);

    @Async
    Future<Book> queryByIsbn(String isbn);
}

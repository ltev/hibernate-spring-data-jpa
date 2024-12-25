package com.ltev.bookdb.repository;

import com.ltev.bookdb.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitle(String title);

    @Nullable
    Book findByIsbn(@Nullable String isbn);
}

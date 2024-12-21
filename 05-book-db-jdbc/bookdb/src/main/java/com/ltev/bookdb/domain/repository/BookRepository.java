package com.ltev.bookdb.domain.repository;

import com.ltev.bookdb.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}

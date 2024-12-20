package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book, Integer> {

}

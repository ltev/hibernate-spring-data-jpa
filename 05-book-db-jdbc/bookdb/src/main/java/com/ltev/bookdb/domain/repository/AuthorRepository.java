package com.ltev.bookdb.domain.repository;

import com.ltev.bookdb.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}

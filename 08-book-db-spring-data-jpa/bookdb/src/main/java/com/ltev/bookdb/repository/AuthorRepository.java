package com.ltev.bookdb.repository;

import com.ltev.bookdb.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByLastName(String lastName);

    List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select a from Author a left join fetch a.books where a.id = ?1")
    Optional<Author> findByIdJoinFetchBooks(Long id);
}

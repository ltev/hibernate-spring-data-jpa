package com.ltev.bookdb.dao;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;

import java.util.List;
import java.util.Optional;

public interface AuthorDao extends BaseDao<Author, Long> {

    List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    List<Book> findBooks(Long authorId);

    Optional<Author> findByIdJoinFetchBooks(Long authorId);
}

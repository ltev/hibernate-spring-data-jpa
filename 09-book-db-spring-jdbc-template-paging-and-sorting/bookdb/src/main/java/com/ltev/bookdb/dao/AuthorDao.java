package com.ltev.bookdb.dao;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorDao extends BaseDao<Author, Long> {

    int saveInBatch(List<Author> authors);

    List<Book> findBooks(Long authorId);

    Optional<Author> findByIdJoinFetchBooks(Long authorId);

    List<Author> findByFirstNameAndLastName(String firstName, String lastName);

    List<Author> findByLastNameSortByFirstName(String lastName);

    default List<Author> findByLastNameSortByFirstName(String lastName, Pageable pageable) {
        throw new UnsupportedOperationException();
    }
}

package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDaoImpl extends AbstractDaoImpl<Author> implements AuthorDao {

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        return null;
    }

    @Override
    public List<Book> findBooks(Long authorId) {
        return null;
    }

    @Override
    public Optional<Author> findByIdJoinFetchBooks(Long authorId) {
        return Optional.empty();
    }
}
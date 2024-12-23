package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDaoImpl extends AbstractDaoImpl<Author> implements AuthorDao {

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        return null;
    }

    @Override
    public int saveInBatch(List<Author> authors) {
        return 0;
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

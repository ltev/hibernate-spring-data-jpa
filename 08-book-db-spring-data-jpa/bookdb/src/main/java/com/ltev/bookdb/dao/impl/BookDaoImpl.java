package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Book> implements BookDao {

    public BookDaoImpl(JpaRepository<Book, Long> repository) {
        super(repository);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return null;
    }

    @Override
    public List<Book> findByIsbnLike(String isbnStart) {
        return null;
    }
}

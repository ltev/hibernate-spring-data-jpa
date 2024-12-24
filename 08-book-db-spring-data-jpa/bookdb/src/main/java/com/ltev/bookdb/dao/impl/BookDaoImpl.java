package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Book> implements BookDao {

    @Override
    public List<Book> findByTitle(String title) {
        return null;
    }

    @Override
    public List<Book> findByIsbnLike(String isbnStart) {
        return null;
    }
}

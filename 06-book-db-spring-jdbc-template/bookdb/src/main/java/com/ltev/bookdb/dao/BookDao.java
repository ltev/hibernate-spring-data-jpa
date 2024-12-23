package com.ltev.bookdb.dao;

import com.ltev.bookdb.domain.Book;

import java.util.List;

public interface BookDao extends BaseDao<Book, Long> {

    List<Book> findByTitle(String title);
}

package com.ltev.bookdb.dao;

import com.ltev.bookdb.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BookDao extends BaseDao<Book, Long> {

    List<Book> findByTitle(String title);

    List<Book> findAll();

    List<Book> findAll(int pageSize, int offset);

    default List<Book> findAll(Pageable pageable, Sort sort) {
        throw new UnsupportedOperationException();
    }

    List<Book> findAllByTitle(String title, int pageSize, int offset);

    List<Book> findAllByTitle(String title, Pageable pageable);

    default List<Book> findAllSortByTitleAsc() {
        throw new UnsupportedOperationException();
    }
}

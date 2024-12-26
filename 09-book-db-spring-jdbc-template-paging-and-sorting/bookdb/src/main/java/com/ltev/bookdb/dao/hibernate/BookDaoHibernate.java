package com.ltev.bookdb.dao.hibernate;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoHibernate extends AbstractDaoHibernate<Book> implements BookDao {

    public BookDaoHibernate(EntityManagerFactory emf) {
        super(emf, Book.class);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public List<Book> findAll(int pageSize, int offset) {
        return null;
    }

    @Override
    public List<Book> findAll(Pageable pageable, Sort sort) {
        return BookDao.super.findAll(pageable, sort);
    }

    @Override
    public List<Book> findAllByTitle(String title, int pageSize, int offset) {
        return null;
    }

    @Override
    public List<Book> findAllByTitle(String title, Pageable pageable) {
        return null;
    }

    @Override
    public List<Book> findAllSortByTitleAsc() {
        return BookDao.super.findAllSortByTitleAsc();
    }
}

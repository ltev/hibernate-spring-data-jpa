package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Book> implements BookDao {

    @Override
    public List<Book> findByTitle(String title) {
        return null;
    }
}

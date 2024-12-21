package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Book> implements BookDao {

    public static final String INSERT_SQL = "insert into book (title, publisher, isbn) values (?, ?, ?)";
    public static final String FIND_BY_TITLE_SQL = "select * from book where title = ?";

    public BookDaoImpl(DataSource dataSource) {
        super(dataSource, "book");
    }

    @Override
    public List<Book> findByTitle(String title) {
        return findBy(FIND_BY_TITLE_SQL, List.of(title));
    }

    @Override
    protected String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    protected void setInsertParameters(Book entity, PreparedStatement ps) throws SQLException {
        ps.setString(1, entity.getTitle());
        ps.setString(2, entity.getPublisher());
        ps.setString(3, entity.getIsbn());
    }

    @Override
    protected void updateRow(Book entity, ResultSet rs) throws SQLException {
        rs.updateString("title", entity.getTitle());
        rs.updateString("publisher", entity.getPublisher());
        rs.updateString("isbn", entity.getIsbn());
    }

    @Override
    protected Book getEntityFromRS(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong(1));
        book.setTitle(rs.getString(2));
        book.setPublisher(rs.getString(3));
        book.setIsbn(rs.getString(4));
        return book;
    }
}

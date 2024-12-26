package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class BookDaoImpl extends AbstractDaoImpl<Book> implements BookDao {

    public static final String INSERT_SQL = "insert into book (title, publisher, isbn, author_id) values (?, ?, ?, ?)";
    public static final String FIND_BY_TITLE_SQL = "select * from book where title = ?";

    private final AuthorDao authorDao;

    public BookDaoImpl(DataSource dataSource, AuthorDao authorDao) {
        super(dataSource, "book");
        this.authorDao = authorDao;
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

        if (entity.getAuthor() != null) {
            ps.setLong(4, entity.getAuthor().getId());
        } else {
            ps.setNull(4, Types.BIGINT);
        }
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

        long authorId = rs.getLong(5);
        if (authorId != 0) {
            book.setAuthor(authorDao.findById(authorId).orElseThrow(
                    () -> new RuntimeException("Author not found for id " + authorId))
            );
        }
        return book;
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
    public List<Book> findAllByTitle(String title, int pageSize, int offset) {
        return null;
    }

    @Override
    public List<Book> findAllByTitle(String title, Pageable pageable) {
        return null;
    }
}

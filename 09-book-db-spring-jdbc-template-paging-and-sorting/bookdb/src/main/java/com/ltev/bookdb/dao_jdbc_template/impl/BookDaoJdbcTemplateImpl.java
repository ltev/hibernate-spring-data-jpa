package com.ltev.bookdb.dao_jdbc_template.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.dao.BookDao;
import com.ltev.bookdb.domain.Book;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Primary
@Repository
public class BookDaoJdbcTemplateImpl extends AbstractDaoJdbcTemplateImpl<Book> implements BookDao {

    @AllArgsConstructor
    static class BookRowMapper implements RowMapper<Book> {

        private AuthorDao authorDao;

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
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
    }

    public static final String INSERT_SQL = "insert into book (title, publisher, isbn, author_id) values (?, ?, ?, ?)";
    public static final String UPDATE_SQL = "update book set title=?, publisher=?, isbn=?, author_id=? where id=?";
    public static final String FIND_BY_TITLE_SQL = "select * from book where title = ?";

    private final BookRowMapper bookRowMapper;

    public BookDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate, AuthorDaoJdbcTemplateImpl authorDao) {
        super(jdbcTemplate, "book", new BookRowMapper(authorDao));
        this.bookRowMapper = new BookRowMapper(authorDao);
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jdbcTemplate.query(FIND_BY_TITLE_SQL, rowMapper, title);
    }

    @Override
    protected String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    protected Object[] getInsertParameters(Book entity) {
        Long authorId = entity.getAuthor() != null ? entity.getAuthor().getId() : null;
        return new Object[] {entity.getTitle(), entity.getPublisher(), entity.getIsbn(), authorId};
    }

    @Override
    protected String getUpdateSql() {
        return UPDATE_SQL;
    }

    @Override
    protected Object[] getUpdateParameters(Book entity) {
        Long authorId = entity.getAuthor() != null ? entity.getAuthor().getId() : null;
        return new Object[] {entity.getTitle(), entity.getPublisher(), entity.getIsbn(), authorId, entity.getId()};
    }
}

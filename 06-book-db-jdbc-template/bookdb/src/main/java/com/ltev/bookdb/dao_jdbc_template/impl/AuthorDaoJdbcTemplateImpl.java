package com.ltev.bookdb.dao_jdbc_template.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.domain.Book;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AuthorDaoJdbcTemplateImpl extends AbstractDaoJdbcTemplateImpl<Author> implements AuthorDao {

    private static class AuthorRowMapper implements RowMapper<Author> {

        private static final AuthorRowMapper instance = new AuthorRowMapper();

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong(1));
            author.setFirstName(rs.getString(2));
            author.setLastName(rs.getString(3));
            return author;
        }
    }

    private static class AuthorExtractor implements ResultSetExtractor<Author> {

        private static final AuthorExtractor instance = new AuthorExtractor();
        private static final int BOOK_START_COLUMN_IDX = 4;

        @Override
        public Author extractData(ResultSet rs) throws SQLException, DataAccessException {
            Author author = null;
            List<Book> books = new ArrayList<>();

            while (rs.next()) {
                if (author == null) {
                    author = AuthorRowMapper.instance.mapRow(rs, -1);
                }
                if (rs.getObject(BOOK_START_COLUMN_IDX) == null) {
                    break;
                }
                books.add(mapBook(rs, author, BOOK_START_COLUMN_IDX));
            }

            if (author != null) {
                author.setBooks(books);
            }
            return author;
        }

        private Book mapBook(ResultSet rs, Author author, int startColumn) throws SQLException {
            int columnIdx = startColumn;

            Book book = new Book();
            book.setId(rs.getLong(columnIdx++));
            book.setTitle(rs.getString(columnIdx++));
            book.setPublisher(rs.getString(columnIdx++));
            book.setIsbn(rs.getString(columnIdx));

            book.setAuthor(author);
            return book;
        }
    }

    public static final String INSERT_SQL = "insert into author (first_name, last_name) values (?, ?)";
    public static final String UPDATE_SQL = "update author set first_name=?, last_name=? where id=?";
    public static final String FIND_BY_FIRST_NAME_AND_LAST_NAME = "select * from author where first_name = ? and last_name = ?";
    public static final String FIND_BY_ID_JOIN_FETCH_BOOKS_SQL = """
                SELECT a.id as author_id, a.first_name, a.last_name, b.id as book_id, b.title, b.publisher, b.isbn
                FROM author a
                LEFT OUTER JOIN book b ON b.author_id = a.id
                WHERE a.id = ?""";


    public AuthorDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "author");
    }

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        return jdbcTemplate.query(FIND_BY_FIRST_NAME_AND_LAST_NAME, getRowMapper(), firstName, lastName);
    }

    /**
     * SimpleJdbcInsert.class
     *
     * Insert into db when id == null
     * Update db row when id != null
     *
     * @param entity
     * @return original object
     */
    @Override
    public Author save(Author entity) {
        if (entity.getId() != null) {
            return update(entity);
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("author")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("first_name", entity.getFirstName());
        parameters.put("last_name", entity.getLastName());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        entity.setId(id);
        return entity;
    }

    @Override
    public int saveInBatch(List<Author> authors) {
        List<Object[]> args = new ArrayList<>();
        for (int i = 0; i < authors.size(); i++) {
            Author author = authors.get(i);
            Object[] arg = new Object[2];
            arg[0] = author.getFirstName();
            arg[1] = author.getLastName();
            args.add(arg);
        }
        int[] arrAffected = jdbcTemplate.batchUpdate(INSERT_SQL, args);
        return Arrays.stream(arrAffected).sum();
    }

    @Override
    public List<Book> findBooks(Long authorId) {
        return jdbcTemplate.query("select * from book where author_id = ?",
                new BookDaoJdbcTemplateImpl.BookRowMapper(this), authorId);
    }

    @Override
    public Optional<Author> findByIdJoinFetchBooks(Long authorId) {
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_ID_JOIN_FETCH_BOOKS_SQL, AuthorExtractor.instance, authorId));
    }

    @Override
    protected String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    protected Object[] getInsertParameters(Author author) {
        return new Object[] {author.getFirstName(), author.getLastName()};
    }

    @Override
    protected String getUpdateSql() {
        return UPDATE_SQL;
    }

    @Override
    protected Object[] getUpdateParameters(Author entity) {
        return new Object[] {entity.getFirstName(), entity.getLastName(), entity.getId()};
    }

    @Override
    protected RowMapper<Author> getRowMapper() {
        return AuthorRowMapper.instance;
    }
}

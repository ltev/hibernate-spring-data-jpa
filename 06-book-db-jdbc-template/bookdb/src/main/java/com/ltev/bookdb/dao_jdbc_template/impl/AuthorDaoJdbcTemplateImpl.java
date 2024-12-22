package com.ltev.bookdb.dao_jdbc_template.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import org.springframework.jdbc.core.JdbcTemplate;
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

    public static final String INSERT_SQL = "insert into author (first_name, last_name) values (?, ?)";
    public static final String UPDATE_SQL = "update author set first_name=?, last_name=? where id=?";
    public static final String FIND_BY_FIRST_NAME_AND_LAST_NAME = "select * from author where first_name = ? and last_name = ?";

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

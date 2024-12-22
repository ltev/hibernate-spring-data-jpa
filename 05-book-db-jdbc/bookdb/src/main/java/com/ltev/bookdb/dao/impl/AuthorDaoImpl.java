package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AuthorDaoImpl extends AbstractDaoImpl<Author> implements AuthorDao {

    public static final String INSERT_SQL = "insert into author (first_name, last_name) values (?, ?)";
    public static final String FIND_BY_FIRST_NAME_AND_LAST_NAME = "select * from author where first_name = ? and last_name = ?";

    public AuthorDaoImpl(DataSource dataSource) {
        super(dataSource, "author");
    }

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        return findBy(FIND_BY_FIRST_NAME_AND_LAST_NAME, List.of(firstName, lastName));
    }

    @Override
    public int saveInBatch(List<Author> authors) {
        StringBuilder sb = new StringBuilder("insert into author (first_name, last_name) values");
        for (int i = 0; i < authors.size(); i++) {
            sb.append(" (?, ?),");
        }
        String sql = sb.substring(0, sb.length() - 1);

        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            setInsertInBatchParameters(authors, ps);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    protected void setInsertParameters(Author entity, PreparedStatement ps) throws SQLException {
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());
    }

    protected void setInsertInBatchParameters(List<Author> entities, PreparedStatement ps) throws SQLException {
        int i = 1;
        for (Author entity : entities) {
            ps.setString(i++, entity.getFirstName());
            ps.setString(i++, entity.getLastName());
        }
    }

    @Override
    protected void updateRow(Author entity, ResultSet rs) throws SQLException {
        rs.updateString("first_name", entity.getFirstName());
        rs.updateString("last_name", entity.getLastName());
    }

    @Override
    protected Author getEntityFromRS(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getLong(1));
        author.setFirstName(rs.getString(2));
        author.setLastName(rs.getString(3));
        return author;
    }
}

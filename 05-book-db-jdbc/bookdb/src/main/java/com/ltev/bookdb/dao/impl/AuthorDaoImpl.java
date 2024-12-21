package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: change Statement to PreparedStatement
 */
@Repository
public class AuthorDaoImpl extends AbstractDaoImpl<Author> implements AuthorDao {

    public AuthorDaoImpl(DataSource dataSource) {
        super(dataSource, "author");
    }

    @Override
    public List<Author> findByFirstNameAndLastName(String firstName, String lastName) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("select * from author where first_name = ? and last_name = ?")){
            ps.setString(1, firstName);
            ps.setString(2, lastName);

            try (ResultSet rs = ps.executeQuery()) {
                List<Author> authors = new ArrayList<>();

                while (rs.next()) {
                    Author author = getEntityFromRS(rs);
                    authors.add(author);
                }

                return authors;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected String getInsertSql() {
        return "insert into author (first_name, last_name) values (?, ?)";
    }

    @Override
    protected void setInsertParameters(Author entity, PreparedStatement ps) throws SQLException {
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());
    }

    @Override
    protected void updateRow(Author entity, ResultSet rs) throws SQLException {
        rs.updateString("first_name", entity.getFirstName());
        rs.updateString("last_name", entity.getLastName());
    }

    // == PRIVATE HELPER METHODS ==

    private void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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

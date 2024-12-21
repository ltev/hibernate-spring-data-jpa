package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    /**
     * Insert into db when id == null
     * Update db row when id != null
     *
     * @param author
     * @return original object
     */
    @Override
    public Author save(Author author) {
        if (author.getId() != null) {
            return update(author);
        }

        ResultSet rs = null;

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            String sql = String.format(
                    "insert into author (first_name, last_name) values ('%s', '%s')",
                    author.getFirstName(), author.getLastName());

            statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
            rs = statement.getGeneratedKeys();

            if (rs.next()) {
                author.setId(rs.getLong(1));
            } else {
                throw new SQLException("Insert Author failed: " + author);
            }
            connection.commit();
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResource(rs);
        }
    }

    private Author update(Author author) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = statement.executeQuery("select * from author where id = " + author.getId())) {

            if (rs.next()) {
                rs.updateString("first_name", author.getFirstName());
                rs.updateString("last_name", author.getLastName());
                rs.updateRow();
            } else {
                throw new SQLException("Update Author failed: " + author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return author;
    }

    @Override
    public Optional<Author> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("select * from author where id = " + id)) {

            if (rs.next()) {
                Author author = new Author();
                author.setId(rs.getLong(1));
                author.setFirstName(rs.getString(2));
                author.setLastName(rs.getString(3));

                return Optional.of(author);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}

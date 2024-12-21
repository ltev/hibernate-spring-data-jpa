package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.dao.AuthorDao;
import com.ltev.bookdb.domain.Author;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TODO: change Statement to PreparedStatement
 */
@Repository
@AllArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    @Override
    public long count() {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement("select count(*) from author");
            ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);

            String sql = String.format(
                    "insert into author (first_name, last_name) values ('%s', '%s')",
                    author.getFirstName(), author.getLastName());

            statement.execute(sql, Statement.RETURN_GENERATED_KEYS);

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    author.setId(rs.getLong(1));
                } else {
                    throw new SQLException("Insert Author failed: " + author);
                }
            }

            connection.commit();
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

            return rs.next() ? Optional.of(getAuthorFromRS(rs)) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                    Author author = getAuthorFromRS(rs);
                    authors.add(author);
                }

                return authors;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("delete from author where id = ?")) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException("Delete Author failed: " + id + ". Rows affected: " + rowsAffected);
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

    private Author getAuthorFromRS(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setId(rs.getLong(1));
        author.setFirstName(rs.getString(2));
        author.setLastName(rs.getString(3));
        return author;
    }
}

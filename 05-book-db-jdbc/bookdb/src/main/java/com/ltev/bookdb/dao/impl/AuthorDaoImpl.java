package com.ltev.bookdb.dao.impl;

import com.ltev.bookdb.domain.Author;
import com.ltev.bookdb.dao.AuthorDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource dataSource;

    @Override
    public Author save(Author author) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(
                    String.format("insert into author (first_name, last_name) values ('%s', '%s')",
                            author.getFirstName(), author.getLastName()), Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                author.setId(rs.getLong(1));
            } else {
                throw new SQLException("Insert Author failed: " + author);
            }
            connection.commit();
            return author;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from author where id = " + id);

            Author author = null;
            if (rs.next()) {
                author = new Author();
                author.setId(rs.getLong(1));
                author.setFirstName(rs.getString(2));
                author.setLastName(rs.getString(3));
            }
            return Optional.ofNullable(author);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.ltev.bookdbms;

import com.ltev.bookdbms.repository.BookRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookdbmsApplicationTests {

	@Autowired
	BookRepository bookRepository;

	@Order(1)
	@Test
	void testDataSource(@Autowired DataSource dataSource) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			assertThat(connection.getMetaData().getDatabaseProductName()).isEqualTo("MySQL");
		}
	}

	@Order(2)
	@Test
	void testDataSourceConnection(@Autowired DataSource dataSource) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {

			// start count

			PreparedStatement ps = connection.prepareStatement("select count(*) from book");
			ps.execute();
			ResultSet rs = ps.getResultSet();
			rs.next();
			long startCount = rs.getLong(1);

			// insert new book

			ps = connection.prepareStatement("insert into book (title) values (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, "Book title from tests");
			ps.execute();
			rs = ps.getGeneratedKeys();
			rs.next();
			long generatedId = rs.getLong(1);

			ps = connection.prepareStatement("select count(*) from book");
			rs = ps.executeQuery();
			rs.next();
			long countAfterInsertion = rs.getLong(1);

			// assert insertion

			assertThat(countAfterInsertion).isEqualTo(startCount + 1);

			// delete inserted book

			ps = connection.prepareStatement("delete from book where id = ?");
			ps.setLong(1, generatedId);
			int deleted = ps.executeUpdate();

			ps = connection.prepareStatement("select count(*) from book");
			ps.execute();
			rs = ps.getResultSet();
			rs.next();
			long countAfterDeletion = rs.getLong(1);

			// assert deletion

			assertThat(deleted).isEqualTo(1);
			assertThat(countAfterDeletion).isEqualTo(startCount);
		}
	}

	@Test
	void contextLoads() {
		assertThat(bookRepository).isNotNull();
	}

}

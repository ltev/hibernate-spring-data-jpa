package com.ltev.bookdbms;

import com.ltev.bookdbms.bootstrap.DataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest    // replaces db to h2
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)  // do not replace the local mysql db for the default embedded h2
@ComponentScan(basePackageClasses = DataInitializer.class)
public class MySqlIntegrityTest {

    @Test
    void testIfMySqlIsInUse(@Autowired DataSource dataSource) throws SQLException {
        assertThat(dataSource.getConnection().getMetaData().getDatabaseProductName()).isEqualTo("MySQL");
    }
}

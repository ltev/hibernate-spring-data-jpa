package com.ltev.bookdbms.config.flyway;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("clean")
@Configuration
public class DbClean {

    @Bean
    FlywayMigrationStrategy clean() {
        return flyway -> {
            flyway.clean();             // need to be enabled in property file
            flyway.migrate();
        };
    }
}

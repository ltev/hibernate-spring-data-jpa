package com.ltev.bookdbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class AuthorUuid {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(value = Types.VARCHAR)                // persist as a String
    @Column(length = 36, columnDefinition = "varchar(36)", unique = true, updatable = false, nullable = false)
    private UUID id;

    private String firstName;
    private String lastName;

    public AuthorUuid(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

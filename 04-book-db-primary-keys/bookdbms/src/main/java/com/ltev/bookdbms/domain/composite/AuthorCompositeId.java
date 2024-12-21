package com.ltev.bookdbms.domain.composite;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "author_composite")
@IdClass(NameId.class)
@NoArgsConstructor
@Getter
@Setter
public class AuthorCompositeId {

    @Id
    private String firstName;

    @Id
    private String lastName;

    private String country;

    public AuthorCompositeId(String firstName, String lastName, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
    }
}

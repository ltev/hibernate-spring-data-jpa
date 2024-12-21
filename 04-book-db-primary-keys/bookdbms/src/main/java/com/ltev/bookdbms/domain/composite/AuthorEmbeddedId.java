package com.ltev.bookdbms.domain.composite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "author_composite")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorEmbeddedId {

    @EmbeddedId
    private NameId nameId;

    private String country;
}
package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.composite.AuthorEmbeddedId;
import com.ltev.bookdbms.domain.composite.NameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorEmbeddedRepository extends JpaRepository<AuthorEmbeddedId, NameId> {

}
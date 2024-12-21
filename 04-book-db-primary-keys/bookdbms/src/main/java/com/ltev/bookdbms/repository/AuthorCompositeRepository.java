package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.composite.AuthorCompositeId;
import com.ltev.bookdbms.domain.composite.NameId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorCompositeRepository extends JpaRepository<AuthorCompositeId, NameId> {

}
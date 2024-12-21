package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.AuthorUuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorUuidRepository extends JpaRepository<AuthorUuid, Long> {
}
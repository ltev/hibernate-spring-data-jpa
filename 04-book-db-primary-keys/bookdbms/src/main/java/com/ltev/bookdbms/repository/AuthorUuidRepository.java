package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.AuthorUuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorUuidRepository extends JpaRepository<AuthorUuid, UUID> {
}
package com.ltev.bookdbms.repository;

import com.ltev.bookdbms.domain.BookUuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookUuidRepository extends JpaRepository<BookUuid, UUID> {

}

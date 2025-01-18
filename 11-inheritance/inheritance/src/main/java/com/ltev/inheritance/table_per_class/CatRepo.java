package com.ltev.inheritance.table_per_class;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepo extends JpaRepository<Cat, Integer> {
}

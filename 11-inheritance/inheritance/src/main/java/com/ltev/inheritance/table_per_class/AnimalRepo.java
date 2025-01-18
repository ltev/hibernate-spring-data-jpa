package com.ltev.inheritance.table_per_class;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimalRepo extends JpaRepository<Animal, Integer> {

    Optional<Cat> getCatById(int i);
}

package com.ltev.inheritance.mapped_superclass;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepo extends JpaRepository<Person, Integer> {
}

package com.ltev.inheritance.mapped_superclass;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends Person {

    private String name;

    public Teacher(Integer id) {
        super(id);
    }
}

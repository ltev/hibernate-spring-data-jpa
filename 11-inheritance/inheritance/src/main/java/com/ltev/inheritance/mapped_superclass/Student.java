package com.ltev.inheritance.mapped_superclass;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student extends Person {

    private Float averageGrade;

    public Student(Integer id) {
        super(id);
    }
}

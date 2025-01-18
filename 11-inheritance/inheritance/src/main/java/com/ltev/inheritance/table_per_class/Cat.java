package com.ltev.inheritance.table_per_class;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Cat extends Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String agile;
}

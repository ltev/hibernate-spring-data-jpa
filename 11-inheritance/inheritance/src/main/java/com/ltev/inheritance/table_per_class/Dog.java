package com.ltev.inheritance.table_per_class;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dog extends Animal {

    private String furQuality;
}

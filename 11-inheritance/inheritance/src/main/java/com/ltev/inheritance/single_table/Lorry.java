package com.ltev.inheritance.single_table;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Lorry extends Vehicle {

    private Double capacity = 7.5;
}

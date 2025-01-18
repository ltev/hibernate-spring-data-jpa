package com.ltev.inheritance.single_table;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.ToString;

@Entity
@ToString
@DiscriminatorValue(value = "car")      // by default exact class name (with uppercase)
public class Car extends Vehicle {

    private String engineType = "petrol";
}

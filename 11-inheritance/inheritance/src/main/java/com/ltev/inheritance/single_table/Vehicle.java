package com.ltev.inheritance.single_table;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@ToString
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)    // by default - dtype (String)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}

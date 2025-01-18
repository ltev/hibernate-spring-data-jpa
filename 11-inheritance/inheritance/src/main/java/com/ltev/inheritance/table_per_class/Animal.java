package com.ltev.inheritance.table_per_class;

import jakarta.persistence.*;

/**
 * On abstract class no table will be created
 * On concrete class table 'animal' will be created
 * Using AnimalRepo.save will save the object into the corresponding subclass table
 * Only objects of class Animal (not subclasses) will be saved into this 'animal' table
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    private boolean created = true;
}

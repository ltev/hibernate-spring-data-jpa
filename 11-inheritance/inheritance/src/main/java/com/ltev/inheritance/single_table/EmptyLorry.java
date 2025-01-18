package com.ltev.inheritance.single_table;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class EmptyLorry extends Lorry {

    private boolean emptyCapacity = true;

    // same property name as in Lorry -> uses the same column
    private Double capacity = 0d;
}

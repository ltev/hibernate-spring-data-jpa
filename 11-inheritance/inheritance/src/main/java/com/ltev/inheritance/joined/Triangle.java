package com.ltev.inheritance.joined;

import jakarta.persistence.Entity;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
public class Triangle extends Polygon {

    private boolean hasThreeCorners = true;

    public Triangle() {
        super(3);
    }
}

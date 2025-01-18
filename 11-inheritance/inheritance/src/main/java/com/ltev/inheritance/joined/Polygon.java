package com.ltev.inheritance.joined;

import jakarta.persistence.Entity;
import lombok.ToString;

@Entity
@ToString(callSuper = true)
public class Polygon extends Shape {

    private boolean isPolygon = true;
    private int numCorners;

    public Polygon() {
        numCorners = -1;
    }

    public Polygon(int numCorners) {
        this.numCorners = numCorners;
    }
}

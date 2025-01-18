package com.ltev.inheritance.joined;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShapeRepo extends JpaRepository<Shape, Integer> {

    Optional<Triangle> getTriangleById(Integer id);

    Optional<Polygon> getPolygonById(Integer id);
}

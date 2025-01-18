package com.ltev.inheritance.single_table;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {

    Optional<Car> findCarById(Integer id);
}

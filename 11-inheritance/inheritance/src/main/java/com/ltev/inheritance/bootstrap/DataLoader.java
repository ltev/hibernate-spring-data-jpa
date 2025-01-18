package com.ltev.inheritance.bootstrap;

import com.ltev.inheritance.joined.Polygon;
import com.ltev.inheritance.joined.ShapeRepo;
import com.ltev.inheritance.joined.Triangle;
import com.ltev.inheritance.mapped_superclass.*;
import com.ltev.inheritance.single_table.*;
import com.ltev.inheritance.table_per_class.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    // @MappedSuperclass
    private PersonRepo personRepo;
    private StudentRepo studentRepo;
    private TeacherRepo teacherRepo;

    // table per class
    private AnimalRepo animalRepo;
    private HouseDogRepo houseDogRepo;
    private CatRepo catRepo;

    // single table
    private VehicleRepo vehicleRepo;
    private LorryRepo lorryRepo;

    // joined tables
    private ShapeRepo shapeRepo;

    @Override
    public void run(String... args) throws Exception {
        // 1. mapped superclass
        System.out.println("== MAPPED SUPERCLASS ==");

        studentRepo.save(new Student());    // id 1
        teacherRepo.save(new Teacher());    // id 1
        personRepo.save(new Student());     // id 2     -> saved in 'student' table

        // 2. table per class
        System.out.println("== TABLE PER CLASS ==");

        Dog dog = new Dog();
        dog.setFurQuality("amazing");

        /*
         * AnimalRepo saves dog in it's correct dog table with all properties
         * No changes to table 'animal' - it stays empty the whole time
         */

        //dogRepo.save(dog);            // id 1
        animalRepo.save(dog);               // id 1     -> saved in 'dog' table
        houseDogRepo.save(new HouseDog());  // id 2
        catRepo.save(new Cat());            // id 3

        Animal foundAnimal = animalRepo.findById(2).get();      // found HouseDog
        System.out.println(foundAnimal);

        Optional<Cat> foundCat = animalRepo.getCatById(3);     // cat id -> found Cat
        System.out.println(foundCat);

        foundCat = animalRepo.getCatById(2);                   // dog id -> Optional.empty
        System.out.println(foundCat);

        // concrete Animal class
        Animal animal = new Animal();
        animalRepo.save(animal);            // id 4     -> saved in 'animal' table

        animal = new Cat();
        animalRepo.save(animal);            // id 5     -> saved in 'cat' table

        // 3. single table

        System.out.println("== SINGLE TABLE ==");
        lorryRepo.save(new Lorry());
        vehicleRepo.save(new Car());
        vehicleRepo.save(new EmptyLorry());

        System.out.println(vehicleRepo.findById(1));
        System.out.println(vehicleRepo.findById(2));

        System.out.println(vehicleRepo.findCarById(1));     // Optional.empty   -> Lorry type
        System.out.println(vehicleRepo.findCarById(2));

        // 4. joined tables

        System.out.println("== JOINED TABLES ==");
        shapeRepo.save(new Polygon());              // table 'shape' + 'polygon'
        shapeRepo.save(new Triangle());             // table 'shape' + 'polygon' + 'triangle'

        System.out.println(shapeRepo.getPolygonById(1));
        System.out.println(shapeRepo.getPolygonById(2));

        System.out.println(shapeRepo.getTriangleById(1));   // Optional.empty
        System.out.println(shapeRepo.getTriangleById(2));
    }
}

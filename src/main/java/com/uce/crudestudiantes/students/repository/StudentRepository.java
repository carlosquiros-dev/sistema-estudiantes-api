package com.uce.crudestudiantes.students.repository;

import com.uce.crudestudiantes.students.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository automatically provides all basic CRUD operations (save, find, delete)
}
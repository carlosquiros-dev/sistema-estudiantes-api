package com.uce.crudestudiantes.estudiantes.repository;

import com.uce.crudestudiantes.estudiantes.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository provee automáticamente todas las operaciones básicas del CRUD (guardar, buscar, eliminar)
}
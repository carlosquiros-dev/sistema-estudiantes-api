package com.uce.crudestudiantes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // JpaRepository provee automáticamente todas las operaciones básicas del CRUD (guardar, buscar, eliminar)
}
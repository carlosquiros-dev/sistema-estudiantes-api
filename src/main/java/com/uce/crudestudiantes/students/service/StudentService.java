package com.uce.crudestudiantes.students.service;

import com.uce.crudestudiantes.students.model.Student;
import com.uce.crudestudiantes.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // CREATE or UPDATE
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // READ (All)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // READ (By ID)
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // DELETE
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
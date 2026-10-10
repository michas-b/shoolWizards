package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable long studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/age")
    public ResponseEntity<Collection<Student>> getStudentsByAge(@RequestParam(required = false) Integer age) {
        if (age != null && age > 0) return ResponseEntity.ok(studentService.getStudentsAge(age.intValue()));
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PutMapping("{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable long studentId, @RequestBody Student student) {
        student.setId(studentId);
        Student updatedStudent = studentService.updateStudent(studentId, student);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

}
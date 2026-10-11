package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    public Student updateStudent(long studentId, Student student) {
        Optional<Student> foundStudent = studentRepository.findById(studentId);
        if (foundStudent.isPresent()) {
            Student studentToUpdate = foundStudent.get();
            studentToUpdate.setName(student.getName());
            studentToUpdate.setAge(student.getAge());
            return studentRepository.save(studentToUpdate);
        }
        return null;
    }

    public Collection<Student> getStudentsAge(int studentAge) {
        return studentRepository.findByAge(studentAge);
    }

    public Collection<Student> getStudentsBetweenByAge(int min, int max) {
        if (min > 0 && max >= min) {
            return studentRepository.findByAgeBetween(min, max);
        } else {
            return Collections.emptyList();
        }
    }

    public Faculty getFacultyOfStudent(long studentId) {
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public void deleteStudent(long studentId) {
        studentRepository.deleteById(studentId);
    }
}

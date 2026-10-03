package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private Map<Long, Student> studentsMap = new HashMap<>();
    private Long id = 1L;

    public Student createStudent(Student student) {
        student.setId(id);
        studentsMap.put(student.getId(), student);
        id++;
        return student;
    }

    public Student getStudentById(Long studentId) {
        return studentsMap.get(studentId);
    }

    public Student updateStudent(Long studentId, Student student) {
        if (!studentsMap.containsKey(student.getId())) {
            return null;
        }
        studentsMap.put(student.getId(), student);
        return student;
    }

    public Collection<Student> getStudentsAge(int studentAge) {
        return studentsMap.values().stream()
                .filter(student -> student.getAge() == studentAge)
                .collect(Collectors.toList());
    }

    public Student deleteStudent(Long studentId) {
        return studentsMap.remove(studentId);
    }
}

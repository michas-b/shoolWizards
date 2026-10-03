package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private Map<Long, Faculty> facultyMap = new HashMap<>();
    private Long id = 1L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(id);
        facultyMap.put(faculty.getId(), faculty);
        id++;
        return faculty;
    }

    public Faculty getFacultyById(Long facultyId) {
        return facultyMap.get(facultyId);
    }

    public Collection<Faculty> getFacultyByColors(String color) {
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(Long facultyId) {
        return facultyMap.remove(facultyId);
    }
}

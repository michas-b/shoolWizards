package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(long facultyId) {
        return facultyRepository.findById(facultyId).orElse(null);
    }

    public Collection<Faculty> getFacultyByColors(String color) {
        return facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    public Faculty updateFaculty(long facultyId, Faculty faculty) {
        Optional<Faculty> foundFaculty = facultyRepository.findById(facultyId);
        if (foundFaculty.isPresent()) {
            Faculty FacultyToUpdate = foundFaculty.get();
            FacultyToUpdate.setName(faculty.getName());
            FacultyToUpdate.setColor(faculty.getColor());
            return facultyRepository.save(FacultyToUpdate);
        }
        return null;
    }

    public void deleteFaculty(long facultyId) {
        facultyRepository.deleteById(facultyId);
    }
}

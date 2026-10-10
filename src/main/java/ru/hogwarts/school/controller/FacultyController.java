package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequestMapping("/faculty")
@RestController
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaculty);
    }

    @GetMapping("{facultyId}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long facultyId) {
        Faculty faculty = facultyService.getFacultyById(facultyId);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/color")
    public ResponseEntity<Collection<Faculty>> getFacultyByColor(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.getFacultyByColors(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PutMapping("{facultyId}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable long facultyId, @RequestBody Faculty faculty) {
        faculty.setId(facultyId);
        Faculty updatedFaculty = facultyService.updateFaculty(facultyId, faculty);
        if (updatedFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("{facultyId}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable long facultyId) {
        facultyService.deleteFaculty(facultyId);
        return ResponseEntity.noContent().build();
    }
}

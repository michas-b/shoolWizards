package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/student")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{student_id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long uploadAvatar(@PathVariable(name = "student_id") Long studentId,
                             @RequestParam MultipartFile avatarFile) throws IOException {
        return avatarService.uploadAvatar(studentId, avatarFile);
    }

    @GetMapping(value = "/{student_id}/avatar")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable(name = "student_id") Long studentId) {
        Avatar avatar = avatarService.getAvatar(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avatar not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.ok().headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{student_id}/avatar/preview")
    public ResponseEntity<byte[]> getAvatarFromDisk(@PathVariable(name = "student_id") Long studentId) throws IOException {
        Avatar avatar = avatarService.getAvatar(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avatar not found"));

        byte[] data = avatarService.getAvatarDataFromDisk(studentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(data.length);

        return ResponseEntity.ok().headers(headers).body(data);
    }
}
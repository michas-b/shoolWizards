package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@Transactional
public class AvatarService {

    private static final int PREVIEW_WIDTH = 100;
    private static final String DEFAULT_EXTENSION = "jpg";

    @Value("${student.avatar.dir}")
    private String avatarDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Long uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        String extension = getExtension(avatarFile.getOriginalFilename());
        Path filePath = Path.of(avatarDir, studentId + "." + extension);

        Files.createDirectories(filePath.getParent());
        try (InputStream inputStream = avatarFile.getInputStream();
             OutputStream outputStream = Files.newOutputStream(filePath)) {
            inputStream.transferTo(outputStream);
        }

        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatar.setStudent(student);

        Avatar savedAvatar = avatarRepository.save(avatar);
        return savedAvatar.getId();
    }

    public Optional<Avatar> getAvatar(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }

    public byte[] getAvatarDataFromDisk(Long studentId) throws IOException {
        Avatar avatar = avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Avatar not found"));
        Path filePath = Path.of(avatar.getFilePath());
        return Files.readAllBytes(filePath);
    }

    public byte[] generatePreview(Long studentId) throws IOException {
        Avatar avatar = avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Avatar not found"));

        Path filePath = Path.of(avatar.getFilePath());
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Could not read image");
            }

            int height = PREVIEW_WIDTH * image.getHeight() / image.getWidth();
            BufferedImage preview = new BufferedImage(PREVIEW_WIDTH, height, image.getType());

            Graphics2D g2d = preview.createGraphics();
            g2d.drawImage(image, 0, 0, PREVIEW_WIDTH, height, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null) {
            return DEFAULT_EXTENSION;
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return DEFAULT_EXTENSION;
        }
        return fileName.substring(lastDotIndex + 1);
    }
}
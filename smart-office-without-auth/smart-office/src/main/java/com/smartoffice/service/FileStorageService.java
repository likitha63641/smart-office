package com.smartoffice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;
    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try { Files.createDirectories(rootLocation); }
        catch (Exception e) { throw new RuntimeException("Could not create upload directory", e); }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalStateException("Attachment is empty");
        String original = file.getOriginalFilename() == null ? "attachment" : Paths.get(file.getOriginalFilename()).getFileName().toString();
        String safeName = UUID.randomUUID() + "_" + original.replaceAll("[^a-zA-Z0-9._-]", "_");
        try {
            Path target = rootLocation.resolve(safeName).normalize();
            if (!target.startsWith(rootLocation)) throw new IllegalStateException("Invalid attachment name");
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return safeName;
        } catch (Exception e) { throw new RuntimeException("Could not store file", e); }
    }
}

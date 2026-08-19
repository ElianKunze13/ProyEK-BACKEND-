// ImagenUploadController.java
package com.example.demo.controller;

import com.example.demo.dto.ImagenDto;
import com.example.demo.service.ImagenUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ImagenUploadController {

    private final ImagenUploadService imagenUploadService;

    @PostMapping("/auth/upload/imagen")
    public ResponseEntity<ImagenDto> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("📤 Recibida solicitud de subida de imagen: {}", file.getOriginalFilename());

        try {
            ImagenDto imagenDto = imagenUploadService.uploadImageToImgBB(file);
            return ResponseEntity.ok(imagenDto);
        } catch (Exception e) {
            log.error("❌ Error al subir imagen: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
// ImagenUploadController.java - SIN CAMBIOS
package com.example.demo.controller;

import com.example.demo.dto.ImagenDto;
import com.example.demo.service.ImagenUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ImagenUploadController {

    private final ImagenUploadService imagenUploadService;

    @PostMapping(value = "/auth/upload/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagenDto> uploadImage(
            @RequestParam(value = "file", required = true) MultipartFile file) {

        log.info("📤 Recibida solicitud de subida de imagen");
        log.info("📄 Nombre: {}", file.getOriginalFilename());
        log.info("📏 Tamaño: {} bytes", file.getSize());
        log.info("📋 Tipo: {}", file.getContentType());

        try {
            ImagenDto imagenDto = imagenUploadService.uploadImageToImgBB(file);
            log.info("✅ Imagen subida exitosamente: {}", imagenDto.getUrl());
            return ResponseEntity.ok(imagenDto);
        } catch (Exception e) {
            log.error("❌ Error al subir imagen: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
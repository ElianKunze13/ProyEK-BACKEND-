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
        log.info("📤 Recibida solicitud de subida de imagen");
        log.info("📄 Nombre: {}", file.getOriginalFilename());
        log.info("📏 Tamaño: {} bytes", file.getSize());
        log.info("📋 Tipo: {}", file.getContentType());
        log.info("🔗 Headers: {}", file.getName());

        try {
            // Validar archivo
            if (file.isEmpty()) {
                log.error("❌ El archivo está vacío");
                return ResponseEntity.badRequest().build();
            }

            ImagenDto imagenDto = imagenUploadService.uploadImageToImgBB(file);
            log.info("✅ Imagen subida exitosamente: {}", imagenDto.getUrl());
            return ResponseEntity.ok(imagenDto);

        } catch (IllegalArgumentException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("❌ Error al subir imagen: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
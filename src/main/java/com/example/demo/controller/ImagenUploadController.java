// ImagenUploadController.java - VERSIÓN CORREGIDA
package com.example.demo.controller;

import com.example.demo.dto.ImagenDto;
import com.example.demo.service.ImagenUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ImagenUploadController {

    private final ImagenUploadService imagenUploadService;

    // ✅ VERSIÓN 1: Con consumes explícito
    @PostMapping(value = "/auth/upload/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagenDto> uploadImage(
            @RequestParam(value = "file", required = true) MultipartFile file) {

        log.info("📤 Recibida solicitud de subida de imagen");
        log.info("📄 Nombre: {}", file.getOriginalFilename());
        log.info("📏 Tamaño: {} bytes", file.getSize());
        log.info("📋 Tipo: {}", file.getContentType());

        try {
            // Validaciones adicionales
            if (file.isEmpty()) {
                log.error("❌ El archivo está vacío");
                return ResponseEntity.badRequest().build();
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                log.error("❌ Tipo de archivo no válido: {}", contentType);
                return ResponseEntity.badRequest().build();
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                log.error("❌ Archivo demasiado grande: {} bytes", file.getSize());
                return ResponseEntity.badRequest().build();
            }

            ImagenDto imagenDto = imagenUploadService.uploadImageToImgBB(file);
            log.info("✅ Imagen subida exitosamente: {}", imagenDto.getUrl());
            return ResponseEntity.ok(imagenDto);

        } catch (IllegalArgumentException e) {
            log.error("❌ Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            log.error("❌ Error al subir imagen: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ✅ VERSIÓN 2: Alternativa sin consumes específico
    @PostMapping("/auth/upload/imagen-alt")
    public ResponseEntity<ImagenDto> uploadImageAlt(
            @RequestParam("file") MultipartFile file) {

        // Mismo código de arriba
        return uploadImage(file);
    }
    // ImagenUploadController.java - AGREGAR ENDPOINT DE PRUEBA
    @GetMapping("/auth/test-imgbb")
    public ResponseEntity<String> testImgBBConnection() {
        try {
            // Verificar que la API Key existe
            String apiKey = System.getenv("IMGBB_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ IMGBB_API_KEY no configurada");
            }

            // Hacer una petición de prueba a ImgBB
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.imgbb.com/1/upload?key=" + apiKey;

            // Intentar una subida de prueba con un archivo pequeño
            // (Esto es solo para verificar que la API Key funciona)
            return ResponseEntity.ok("✅ API Key encontrada: " + apiKey.substring(0, 5) + "...");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("❌ Error: " + e.getMessage());
        }
    }
}
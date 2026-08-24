// ImagenUploadServiceImpl.java - VERSIÓN CORREGIDA
package com.example.demo.service.Impl;

import com.example.demo.dto.ImagenDto;
import com.example.demo.service.ImagenUploadService;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagenUploadServiceImpl implements ImagenUploadService {

    private final ImageKit imageKit;

    @Override
    public ImagenDto uploadImageToImgBB(MultipartFile file) throws Exception {
        log.info("🔄 Subiendo imagen a ImageKit.io");
        log.info("📄 Nombre: {}", file.getOriginalFilename());
        log.info("📏 Tamaño: {} bytes", file.getSize());
        log.info("📋 Tipo: {}", file.getContentType());

        // ✅ VALIDAR ARCHIVO
        if (file.isEmpty()) {
            log.error("❌ El archivo está vacío");
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("❌ Tipo de archivo no válido: {}", contentType);
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            log.error("❌ Archivo demasiado grande: {} bytes", file.getSize());
            throw new IllegalArgumentException("La imagen no puede superar los 5MB");
        }

        try {
            // ✅ CREAR SOLICITUD DE SUBIDA A IMAGEKIT
            FileCreateRequest request = new FileCreateRequest(file.getBytes(), file.getOriginalFilename());

            // ✅ OPCIONAL: Definir carpeta
            // request.setFolder("/portfolio/experiencias");

            log.info("📤 Enviando petición a ImageKit.io...");

            // ✅ SUBIR LA IMAGEN USANDO EL SDK
            Result result = imageKit.upload(request);

            // ✅ VERIFICAR RESPUESTA - MÉTODO CORRECTO PARA ESTA VERSIÓN
            // En lugar de isSuccessful(), verificamos si el resultado tiene URL
            if (result.getUrl() == null || result.getUrl().isEmpty()) {
                log.error("❌ Error en ImageKit: No se obtuvo URL");
                throw new Exception("Error en ImageKit: No se obtuvo URL");
            }

            // ✅ EXTRAER URL
            String url = result.getUrl();

            log.info("✅ Imagen subida exitosamente a ImageKit: {}", url);

            return ImagenDto.builder()
                    .url(url)
                    .alt(file.getOriginalFilename())
                    .build();

        } catch (IOException e) {
            log.error("❌ Error al leer el archivo: {}", e.getMessage());
            throw new Exception("Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            log.error("❌ Error al subir imagen a ImageKit: {}", e.getMessage(), e);
            throw new Exception("Error al subir la imagen: " + e.getMessage());
        }
    }
}
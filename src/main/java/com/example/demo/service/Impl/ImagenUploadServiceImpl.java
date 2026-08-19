// ImagenUploadServiceImpl.java
package com.example.demo.service.Impl;

import com.example.demo.dto.ImagenDto;
import com.example.demo.service.ImagenUploadService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagenUploadServiceImpl implements ImagenUploadService {

    @Value("${imgbb.api.key}")
    private String imgbbApiKey;

    private final String IMGBB_UPLOAD_URL = "https://api.imgbb.com/1/upload";

    @Override
    public ImagenDto uploadImageToImgBB(MultipartFile file) throws Exception {
        log.info("🔄 Subiendo imagen a ImgBB: {}", file.getOriginalFilename());

        // Validar archivo
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        // Validar tipo de archivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // Validar tamaño (máx 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("La imagen no puede superar los 5MB");
        }

        try {
            // Preparar la petición a ImgBB
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", imgbbApiKey);
            body.add("image", new MultipartFileResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            // Hacer la petición
            ResponseEntity<String> response = restTemplate.exchange(
                    IMGBB_UPLOAD_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Parsear respuesta
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if (!root.has("success") || !root.get("success").asBoolean()) {
                throw new RuntimeException("Error en la respuesta de ImgBB: " + response.getBody());
            }

            // Extraer datos
            JsonNode data = root.get("data");
            String url = data.get("url").asText();
            String deleteUrl = data.has("delete_url") ? data.get("delete_url").asText() : null;
            String thumb = data.get("thumb").get("url").asText();

            log.info("✅ Imagen subida exitosamente a ImgBB: {}", url);

            // Crear DTO de respuesta
            return ImagenDto.builder()
                    .url(url)
                    .alt(file.getOriginalFilename())
                    .build();

        } catch (Exception e) {
            log.error("❌ Error al subir imagen a ImgBB: {}", e.getMessage());
            throw new Exception("Error al subir la imagen: " + e.getMessage());
        }
    }
}
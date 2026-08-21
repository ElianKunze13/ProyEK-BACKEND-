// ImagenUploadServiceImpl.java - CON MEJOR MANEJO DE ERRORES
package com.example.demo.service.Impl;

import com.example.demo.dto.ImagenDto;
import com.example.demo.exception.MultipartFileResource;
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
        log.info("🔄 Subiendo imagen a ImgBB");

        // ✅ VERIFICAR QUE LA API KEY NO ESTÉ VACÍA
        if (imgbbApiKey == null || imgbbApiKey.isEmpty()) {
            log.error("❌ IMGBB_API_KEY no configurada");
            throw new Exception("IMGBB_API_KEY no configurada. Configura la variable de entorno.");
        }
        log.info("🔑 API Key encontrada: {}", imgbbApiKey.substring(0, 5) + "...");

        // Validar archivo
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
            // ✅ PREPARAR PETICIÓN CON FORMATO CORRECTO
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", imgbbApiKey);
            body.add("image", new MultipartFileResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            log.info("📤 Enviando petición a ImgBB...");

            // ✅ HACER LA PETICIÓN CON MEJOR MANEJO DE ERRORES
            ResponseEntity<String> response;
            try {
                response = restTemplate.exchange(
                        IMGBB_UPLOAD_URL,
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );
            } catch (Exception e) {
                log.error("❌ Error al conectar con ImgBB: {}", e.getMessage());
                throw new Exception("Error al conectar con ImgBB: " + e.getMessage());
            }

            // ✅ VERIFICAR RESPUESTA
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("❌ Respuesta de ImgBB no exitosa: {}", response.getStatusCode());
                throw new Exception("ImgBB respondió con código: " + response.getStatusCode());
            }

            // Parsear respuesta
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            log.info("📥 Respuesta de ImgBB: {}", response.getBody());

            if (!root.has("success") || !root.get("success").asBoolean()) {
                String errorMsg = root.has("error") ?
                        root.get("error").get("message").asText() :
                        "Error desconocido";
                log.error("❌ Error en ImgBB: {}", errorMsg);
                throw new Exception("Error de ImgBB: " + errorMsg);
            }

            // Extraer datos
            JsonNode data = root.get("data");
            String url = data.get("url").asText();
            String deleteUrl = data.has("delete_url") ? data.get("delete_url").asText() : null;
            String thumb = data.get("thumb").get("url").asText();

            log.info("✅ Imagen subida exitosamente: {}", url);

            // Crear DTO de respuesta
            return ImagenDto.builder()
                    .url(url)
                    .alt(file.getOriginalFilename())
                    .build();

        } catch (Exception e) {
            log.error("❌ Error al subir imagen: {}", e.getMessage(), e);
            throw new Exception("Error al subir la imagen: " + e.getMessage(), e);
        }
    }
}
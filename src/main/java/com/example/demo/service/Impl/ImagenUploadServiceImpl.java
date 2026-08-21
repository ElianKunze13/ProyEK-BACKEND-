package com.example.demo.service.Impl;

import com.example.demo.dto.ImagenDto;
import com.example.demo.service.ImagenUploadService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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


    // ImagenUploadServiceImpl.java - VERSIÓN ALTERNATIVA
    @Override
    public ImagenDto uploadImageToImgBB(MultipartFile file) throws Exception {
        log.info("🔄 Subiendo imagen a ImgBB (versión con API en URL)");


        if (imgbbApiKey == null || imgbbApiKey.isEmpty()) {
            log.info("🔄 Subiendo imagen a ImgBB");

            // ✅ LOG DETALLADO DE LA API KEY
            log.info("🔑 API Key (longitud): {}", imgbbApiKey != null ? imgbbApiKey.length() : 0);
            log.info("🔑 API Key (primeros 10 chars): {}",
                    imgbbApiKey != null && imgbbApiKey.length() > 10 ?
                            imgbbApiKey.substring(0, 10) + "..." : "null");

            // ✅ VERIFICAR QUE LA API KEY NO TENGA ESPACIOS
            if (imgbbApiKey != null) {
                imgbbApiKey = imgbbApiKey.trim(); // ✅ ELIMINAR ESPACIOS
            }

            if (imgbbApiKey == null || imgbbApiKey.isEmpty()) {
                log.error("❌ IMGBB_API_KEY no configurada");
                throw new Exception("IMGBB_API_KEY no configurada");
            }
        }

        try {
            // ✅ USAR API KEY EN LA URL
            String uploadUrl = IMGBB_UPLOAD_URL + "?key=" + imgbbApiKey;

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Convertir MultipartFile a ByteArrayResource
            byte[] fileBytes = file.getBytes();
            ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
            body.add("image", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            log.info("📤 Enviando petición a ImgBB...");
            log.info("📄 Archivo: {}", file.getOriginalFilename());
            log.info("📏 Tamaño: {} bytes", file.getSize());

            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl,  // ✅ API KEY EN URL
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            log.info("📥 Código de respuesta: {}", response.getStatusCode());
            log.info("📥 Respuesta: {}", response.getBody());

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new Exception("ImgBB respondió con código: " + response.getStatusCode());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if (!root.has("success") || !root.get("success").asBoolean()) {
                String errorMsg = root.has("error") ?
                        root.get("error").get("message").asText() :
                        "Error desconocido";
                throw new Exception("Error de ImgBB: " + errorMsg);
            }

            JsonNode data = root.get("data");
            String url = data.get("url").asText();

            log.info("✅ Imagen subida exitosamente: {}", url);

            return ImagenDto.builder()
                    .url(url)
                    .alt(file.getOriginalFilename())
                    .build();

        } catch (Exception e) {
            log.error("❌ Error al subir imagen: {}", e.getMessage(), e);
            throw e;
        }
    }

}
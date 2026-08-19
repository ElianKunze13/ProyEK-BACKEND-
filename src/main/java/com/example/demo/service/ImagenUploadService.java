// ImagenUploadService.java
package com.example.demo.service;

import com.example.demo.dto.ImagenDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImagenUploadService {
    ImagenDto uploadImageToImgBB(MultipartFile file) throws Exception;
}
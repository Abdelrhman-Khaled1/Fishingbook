package com.example.security.imagesWithCloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUpload {
    String uploadProductImage(MultipartFile multipartFile) throws IOException;
}
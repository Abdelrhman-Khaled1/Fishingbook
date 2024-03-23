package com.example.security.imagesWithCloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadImpl implements FileUpload{

    private final Cloudinary cloudinary;

    @Override
    public String uploadProductImage(MultipartFile multipartFile) throws IOException {

        Map<String, String > params = new HashMap<>();
        params.put("folder", "ProductsImages");
        params.put("public_id", UUID.randomUUID().toString());

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),params)
                .get("url")
                .toString();
    }
}

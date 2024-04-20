package com.example.security.imagesWithCloudinary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class FileUploadController {

    private final FileUpload fileUpload;


    @PostMapping("/uploadProductImage")
    public ImageUrl uploadProductImage(@RequestParam("image")MultipartFile multipartFile) throws IOException {
        String imageURL = fileUpload.uploadProductImage(multipartFile);
        return new ImageUrl(imageURL);
    }

    @PostMapping("/uploadUserImage")
    public ImageUrl uploadUserImage(@RequestParam("image")MultipartFile multipartFile) throws IOException {
        String imageURL = fileUpload.uploadUserImage(multipartFile);
        return new ImageUrl(imageURL);
    }
    @PostMapping("/uploadFishImage")
    public ImageUrl uploadFishImage(@RequestParam("image")MultipartFile multipartFile) throws IOException {
        String imageURL = fileUpload.uploadFishImage(multipartFile);
        return new ImageUrl(imageURL);
    }

}

package com.ttn.bootcamp.project.bootcampproject.service;

import jakarta.mail.Multipart;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {
    public String uploadImage(String path, MultipartFile multipartFile) throws IOException {
        String name = multipartFile.getOriginalFilename();
        String filePath = path+ File.separator+name;
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
        }

        Files.copy(multipartFile.getInputStream(), Paths.get(filePath));
        return name;

    }


}

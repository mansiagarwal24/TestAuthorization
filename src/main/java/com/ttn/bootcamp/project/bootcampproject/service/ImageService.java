//package com.ttn.bootcamp.project.bootcampproject.service;
//
//import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
//import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
//import jakarta.mail.Multipart;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Objects;
//import java.util.Optional;
//
//@Service
//public class ImageService {
//    @Autowired
//    UserRepo userRepo;
////    @Value("${path}")
////    String path;
//    public String uploadImage(MultipartFile multipartFile) throws IOException {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepo.findByEmail(email).orElseThrow(null);
//
//        String name = multipartFile.getOriginalFilename();
//        String fileName = String.valueOf(Objects.requireNonNull(user).getUserId()).concat(Objects.requireNonNull(name).substring(name.lastIndexOf(".")));
//
//        String filePath ="/home/mansi/Downloads/bootcamp-project/images/User/"+ fileName;
//
//        Files.deleteIfExists(Path.of(filePath));
//        File file = new File(filePath);
//        if(!file.exists()){
//            file.mkdir();
//        }
//
//        Files.copy(multipartFile.getInputStream(), Paths.get(filePath));
//        user.setProfileImage(fileName);
//        userRepo.save(user);
//        return filePath;
//    }
//
//
//}

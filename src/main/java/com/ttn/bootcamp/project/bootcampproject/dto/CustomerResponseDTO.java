package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CustomerResponseDTO {
    private String firstName;
    private String lastName;
    private Long id;
    private String email;
    private boolean isActive;
    private String image;

}

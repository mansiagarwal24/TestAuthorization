package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SellerUpdateDTO {
    private String firstName;
    private String lastName;
    @Length(min=10,max=10,message = "Phone no must be of 10 digits only!!")
    private String companyContact;
    private String companyName;
    private String middleName;
    private MultipartFile image;
}

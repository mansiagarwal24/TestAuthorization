package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CustomerUpdateDTO {
    private String firstName;
    private String lastName;
    private String middleName;
    @Length(min=10,max=10,message = "Phone no must be of 10 digits only.")
    private String contactNo;
    private MultipartFile image;

}

package com.ttn.bootcamp.project.bootcampproject.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SellerDTO {

    @Pattern(regexp = "^(.+)@(\\S+)$",message = "Email is not valid")
    @Column(unique=true)
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password should contain atleast 8-15 characters with 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    private String confirmPassword;

    @Column(unique=true)
    private String gst;

    @Column(unique=true)
    private String companyName;

    private String companyAddress;
    private Long companyContact;
    private String firstName;
    private String lastName;
}

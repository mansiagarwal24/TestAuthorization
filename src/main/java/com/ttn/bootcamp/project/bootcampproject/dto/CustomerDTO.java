package com.ttn.bootcamp.project.bootcampproject.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerDTO {
    @Email(message = "Email is not valid")
    @NotBlank
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password should contain atleast 8-15 characters with 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    @NotBlank
    private String confirmPassword;


//    @Pattern(regexp = "^(?=.*[0-9])")
//    @Size(min = 10,max = 10,message = "phone no is not valid,must contain 10 digits only")
    @Pattern(regexp="(^[0-9]{10}$)", message = "Contact Number should be of 10 digits.")
    private String phoneNo;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    private String city;
    private String state;
    private String country;
    private String addressLine;
    private int zipCode;
    private String label;

}

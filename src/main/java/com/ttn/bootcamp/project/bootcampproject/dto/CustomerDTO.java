package com.ttn.bootcamp.project.bootcampproject.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerDTO {
    @Email(message = "Email is not valid")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password should contain atleast 8-15 characters with 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    private String confirmPassword;

    @Size(min = 10,max = 10,message = "phone no is not valid,must contain 10 digits only")
    private String phoneNo;
    private String firstName;
    private String lastName;
}

package com.ttn.bootcamp.project.bootcampproject.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
    @Pattern(regexp = "^(.+)@(\\S+)$",message = "Email is not valid")
    @Column(unique=true)
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password should contain atleast 8-15 characters with 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    private String confirmPassword;
//    @Size(min=10,max=10)
    private Long phoneNo;
    private String firstName;
    private String lastName;
}

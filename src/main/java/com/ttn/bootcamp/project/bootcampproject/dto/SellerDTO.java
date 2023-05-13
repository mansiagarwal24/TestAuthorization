package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Address;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SellerDTO {

    @Email(message = "Email is not valid")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password should contain atleast 8-15 characters with 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    @NotBlank
    private String confirmPassword;

    @NotBlank
    @Length(min=15,max=15)
    private String gstNO;

    @NotBlank
    private String companyName;

    private String companyAddress;

//    @Size(max = 10,min = 10)
    @Pattern(regexp="(^[0-9]{10}$)", message = "Contact Number should be of 10 digits.")
    private String companyContact;

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

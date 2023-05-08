package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Address;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SellerDTO {

    @Email(message = "Email is not valid")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password should contain atleast 8-15 characters with 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    private String confirmPassword;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message = "GST must be a valid GST number")
    private String gstNO;

    private String companyName;
    private String companyAddress;

    @Size(max = 10,min = 10)
    private String companyContact;

    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private int zipCode;
    private String label;
}

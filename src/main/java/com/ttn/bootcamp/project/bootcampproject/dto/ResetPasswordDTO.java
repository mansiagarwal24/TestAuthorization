package com.ttn.bootcamp.project.bootcampproject.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetDTO {
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password should contain atleast 8-15 characters with 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;
    private String confirmPassword;
    private String accessToken;
    private String email;
}

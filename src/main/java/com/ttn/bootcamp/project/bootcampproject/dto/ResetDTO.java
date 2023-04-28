package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class ResetDTO {
    private String password;
    private String confirmPassword;
    private String email;
}

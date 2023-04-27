package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType="bearer";

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}

package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    private String response;

    public ResponseDTO(String response) {
        this.response = response;
    }
}

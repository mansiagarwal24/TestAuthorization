package com.ttn.bootcamp.project.bootcampproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariationDTO {
    private Long productId;
    private int quantity;
    private Long price;
    private Map<String,String> metadataValues;
    private boolean isActive;

    private MultipartFile image;

}

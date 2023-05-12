package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductVariationDTO {
    private Long productId;
    private int quantity;
    private Long price;
    private Map<String,String> metadataValues;

}

package com.ttn.bootcamp.project.bootcampproject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Product;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVariationDTO {
    private Product product;
    private int quantity;
    private Long price;
    private Long productId;
    private String metadataValues;
    private String productName;


    private MultipartFile image;

}

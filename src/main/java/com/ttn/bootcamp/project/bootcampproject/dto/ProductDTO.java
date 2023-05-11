package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductDTO {
    @NotBlank
    private String productName;
    @NotBlank
    private String brand;
    private String description;
    private boolean isCancelable;
    private boolean isReturnable;
    @NotNull
    private Long categoryId;




}

package com.ttn.bootcamp.project.bootcampproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryUpdateDTO {
    @NotNull
    private String categoryName;
    private Long parentId;
}

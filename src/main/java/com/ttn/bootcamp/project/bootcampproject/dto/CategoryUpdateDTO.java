package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class CategoryUpdateDTO {
    private String categoryName;
    private Long parentId;
}

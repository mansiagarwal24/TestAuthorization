package com.ttn.bootcamp.project.bootcampproject.entity.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
public class CategoryMetadataField {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String fieldName;

}

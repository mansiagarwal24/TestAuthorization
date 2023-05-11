package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.ttn.bootcamp.project.bootcampproject.Audit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
public class CategoryMetadataField extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String fieldName;

}

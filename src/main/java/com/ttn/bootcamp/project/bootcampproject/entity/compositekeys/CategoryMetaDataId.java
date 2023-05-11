package com.ttn.bootcamp.project.bootcampproject.entity.compositekeys;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMetaDataId implements Serializable {
    private Long categoryId;
    private Long categoryMetadataFieldId;

}

package com.ttn.bootcamp.project.bootcampproject.entity.compositekeys;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class CategoryMetaDataId implements Serializable {
    private Long categoryId;
    private Long categoryMetadataFieldId;

}

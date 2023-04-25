package com.ttn.bootcamp.project.bootcampproject.entities.compositekeys;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CategoryMetaDataId {
    private Long categoryId;
    private Long categoryMetadataFieldId;

    public CategoryMetaDataId(Long categoryId, Long categoryMetadataFieldId) {
        this.categoryId = categoryId;
        this.categoryMetadataFieldId = categoryMetadataFieldId;
    }
}

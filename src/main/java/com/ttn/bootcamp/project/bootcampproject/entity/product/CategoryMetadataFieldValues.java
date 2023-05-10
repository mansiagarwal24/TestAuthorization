package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.CategoryMetaDataId;
import com.ttn.bootcamp.project.bootcampproject.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CategoryMetadataFieldValues {
    @EmbeddedId
    private CategoryMetaDataId categoryMetadataId;

    @ManyToOne
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;

    @ManyToOne
    @MapsId("categoryId")
    private Category category;


    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> value;

}

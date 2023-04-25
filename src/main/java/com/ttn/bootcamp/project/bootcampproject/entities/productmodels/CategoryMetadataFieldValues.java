package com.ttn.bootcamp.project.bootcampproject.entities.productmodels;

import com.ttn.bootcamp.project.bootcampproject.entities.compositekeys.CategoryMetaDataId;
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
    private CategoryMetadataField categoryMetadataFieldList;

    @ManyToOne
    private Category categories;


    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> value;

}

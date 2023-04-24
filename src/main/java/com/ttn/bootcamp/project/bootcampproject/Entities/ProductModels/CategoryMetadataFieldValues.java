package com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels;

import com.ttn.bootcamp.project.bootcampproject.Entities.CompositeKey.CategoryMetaDataId;
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
    @JoinColumn(name = "categoryMetaDataFieldId")
    private CategoryMetadataField categoryMetadataFieldList;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category categories;


    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> value;

}

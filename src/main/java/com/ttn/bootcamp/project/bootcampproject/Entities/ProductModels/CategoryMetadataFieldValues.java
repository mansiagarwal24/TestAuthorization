package com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CategoryMetadataFieldValues {
    //medatdatafieldid(foreign key)
    //categoryid(foreign key)
    @ManyToOne
    @JoinColumn(name = "categoryMetaDataFieldId")
    private CategoryMetadataField categoryMetadataFieldList;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category categories;

    @ElementCollection
    List<String> value;

}

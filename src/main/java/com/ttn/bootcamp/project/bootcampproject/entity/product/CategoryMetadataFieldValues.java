package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttn.bootcamp.project.bootcampproject.Audit;
import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.CategoryMetaDataId;
import com.ttn.bootcamp.project.bootcampproject.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryMetadataFieldValues extends Audit {
    @EmbeddedId
    private CategoryMetaDataId categoryMetadataId;

    @ManyToOne
    @JsonIgnore
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;

    @ManyToOne
    @JsonIgnore
    @MapsId("categoryId")
    private Category category;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> value;

}

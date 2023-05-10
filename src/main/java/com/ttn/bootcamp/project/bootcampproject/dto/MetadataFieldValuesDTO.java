package com.ttn.bootcamp.project.bootcampproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class MetadataFieldValuesDTO {
    private Long categoryId;
    private Long categoryMetaDataFieldId;
    @NotEmpty
    private List<String> values;
}

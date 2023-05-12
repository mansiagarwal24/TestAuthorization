package com.ttn.bootcamp.project.bootcampproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MetadataFieldValuesDTO {
    @NotNull
    private Long categoryId;
    @NotNull
    private Long categoryMetaDataFieldId;
    @NotEmpty
    private Set<String> values;
}

package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.repository.CategoryMetadataField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryMetadataField categoryMetadataField;

    public ResponseEntity<?> addMetadataField(MetaDataFieldDTO metaDataFieldDTO){
        if(categoryMetadataField.existsByName(metaDataFieldDTO.)){

        }
    }

}

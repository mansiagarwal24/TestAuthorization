package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataFieldRepo extends JpaRepository<CategoryMetadataField,Long> {
    boolean existsByFieldName(String fieldName);
}

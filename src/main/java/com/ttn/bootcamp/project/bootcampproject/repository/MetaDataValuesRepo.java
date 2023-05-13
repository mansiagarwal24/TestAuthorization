package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.CategoryMetaDataId;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaDataValuesRepo extends JpaRepository<CategoryMetadataFieldValues, CategoryMetaDataId> {
    CategoryMetadataFieldValues findByCategoryMetadataField(CategoryMetadataField categoryMetadataField);
    CategoryMetadataFieldValues findByCategoryAndCategoryMetadataField(Category category, CategoryMetadataField categoryMetadataField);
    boolean existsByCategoryAndCategoryMetadataField(Category category, CategoryMetadataField categoryMetadataField);

}

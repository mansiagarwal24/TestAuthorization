package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.CategoryMetaDataId;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaDataValuesRepo extends JpaRepository<CategoryMetadataFieldValues, CategoryMetaDataId> {
//    CategoryMetadataFieldValues findByCategoryMetadataId(Long CategoryMetadataId);
}

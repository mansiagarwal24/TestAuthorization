package com.ttn.bootcamp.project.bootcampproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataField extends JpaRepository<CategoryMetadataField,Long> {
    boolean existsByName(String name);
}

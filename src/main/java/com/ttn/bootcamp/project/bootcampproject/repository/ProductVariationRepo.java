package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariationRepo extends JpaRepository<ProductVariation,Long> {
}

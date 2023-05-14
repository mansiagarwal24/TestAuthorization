package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.product.Product;
import com.ttn.bootcamp.project.bootcampproject.entity.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariationRepo extends JpaRepository<ProductVariation,Long> {
    boolean existsByProduct(Product product);

    List<ProductVariation> findByProduct(Product product);
}

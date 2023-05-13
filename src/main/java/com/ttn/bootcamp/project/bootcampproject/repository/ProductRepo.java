package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Product;
import com.ttn.bootcamp.project.bootcampproject.entity.product.ProductVariation;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    boolean existsByBrandAndCategoryAndSeller(String brand, Category category, Seller seller);
//    boolean existsByNameAndSellerId(String name,Long sellerId);
//    List<Product> findAllByName(String name);
    List<Product> findByCategory(Category category);
//    boolean existsByProductVariation(ProductVariation productVariation);

    List<Product> findAllBySeller(Seller seller);

    boolean existsBySeller(Seller seller);
}

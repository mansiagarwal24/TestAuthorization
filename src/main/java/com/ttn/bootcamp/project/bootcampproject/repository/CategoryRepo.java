package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    boolean existsByName(String name);
    boolean existsByParentCategory(Category parentCategory);

    List<Category> findByParentCategory(Category category);
}

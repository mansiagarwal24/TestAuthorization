package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepo extends JpaRepository<Seller,Long> {
    Optional<Seller> findByEmail(String email);
    Optional<Seller> findById(Long userId);
    Boolean existsByEmail(String email);
    Boolean existsByGstNo(String gstNo);


    Boolean existsByCompanyNameIgnoreCase(String companyName);
}

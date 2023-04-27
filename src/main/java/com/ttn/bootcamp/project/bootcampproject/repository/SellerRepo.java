package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepo extends JpaRepository<Seller,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByCompanyName(String companyName);
    Boolean existsByGstNo(String gstNo);

}

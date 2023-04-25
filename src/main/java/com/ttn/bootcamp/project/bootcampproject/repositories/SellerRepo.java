package com.ttn.bootcamp.project.bootcampproject.repositories;

import com.ttn.bootcamp.project.bootcampproject.entities.usermodels.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepo extends JpaRepository<Seller,Long> {
}

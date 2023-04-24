package com.ttn.bootcamp.project.bootcampproject.Repositories;

import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepo extends JpaRepository<Seller,Long> {
}

package com.ttn.bootcamp.project.bootcampproject.Repositories;

import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
}

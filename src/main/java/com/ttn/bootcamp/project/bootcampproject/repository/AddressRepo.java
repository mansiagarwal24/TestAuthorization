package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Address;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepo extends JpaRepository<Address,Long> {
    Optional<Address> findBySeller(Seller seller);
    Optional<Address> findByCustomer(Customer customer);
    boolean existsByCustomer(Customer customer);


}

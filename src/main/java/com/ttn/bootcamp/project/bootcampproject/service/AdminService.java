package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.CustomerResponseDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerResponseDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
@Service
public class AdminService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    TokenRepo tokenRepo;


    public List<CustomerResponseDTO> findAllCustomer(int pageOffSet,int pageSize,String sortBy) {
        PageRequest page = PageRequest.of(pageOffSet,pageSize, Sort.Direction.ASC,sortBy);
        Page<Customer> customersPages = customerRepo.findAll(page);
        List<CustomerResponseDTO> customerList=new ArrayList<>();
        for(Customer customer:customersPages) {
            CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
            customerResponseDTO.setId(customer.getUserId());
            customerResponseDTO.setEmail(customer.getEmail());
            customerResponseDTO.setActive(customer.isActive());
            customerResponseDTO.setFirstName(customer.getFirstName());
            customerResponseDTO.setLastName(customer.getLastName());
            customerList.add(customerResponseDTO);
        }
        return customerList;
    }

    public List<SellerResponseDTO> findAllSeller(int pageOffSet, int pageSize, String sortBy) {
        PageRequest page = PageRequest.of(pageOffSet,pageSize, Sort.Direction.ASC,sortBy);
        Page<Seller> sellersPage = sellerRepo.findAll(page);
        List<SellerResponseDTO> sellerList=new ArrayList<>();
        for(Seller seller:sellersPage) {
            SellerResponseDTO sellerResponseDTO = new SellerResponseDTO();
            sellerResponseDTO.setId(seller.getUserId());
            sellerResponseDTO.setEmail(seller.getEmail());
            sellerResponseDTO.setActive(seller.isActive());
            sellerResponseDTO.setFirstName(seller.getFirstName());
            sellerResponseDTO.setLastName(seller.getLastName());
            sellerResponseDTO.setCompanyName(seller.getCompanyName());
            sellerResponseDTO.setCompanyContact(seller.getCompanyContact());
            sellerResponseDTO.setCompanyAddress(seller.getAddress());
            sellerList.add(sellerResponseDTO);
        }
        return sellerList;
    }

    public boolean activateUser(Long id) {
            User user = userRepo.findById(id).orElseThrow(()-> new ResourcesNotFoundException("User not found"));
            if (!user.isActive()) {
                user.setActive(true);
                user.setLocked(false);
                userRepo.save(user);
                emailService.sendMail(user.getEmail(), "Account Activation Status", "Your account has been activated.");
                return true;
            }
            return false;
    }

    public boolean deactivateUser(Long id) {
            User user = userRepo.findById(id).orElseThrow(()-> new ResourcesNotFoundException("User not found"));
            if (user.isActive()) {
                user.setActive(false);
                user.setLocked(true);
                Token token = tokenRepo.findByEmail(user.getEmail()).orElseThrow();
                token.setDelete(true);
                tokenRepo.save(token);
                userRepo.save(user);
                emailService.sendMail(user.getEmail(), "Account Activation Status", "Your account has been deactivated.");
                return true;
            }
            return false;
        }
}


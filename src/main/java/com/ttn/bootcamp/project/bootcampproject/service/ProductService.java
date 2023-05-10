package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.ProductDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Product;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.ProductRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    EmailService emailService;

    @Value("${admin.email}")
    String adminEmail;

    public void addProduct(ProductDTO productDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->new ResourcesNotFoundException("Seller not found!!"));

        Category category = categoryRepo.findById(productDTO.getCategoryId()).orElseThrow(()->new ResourcesNotFoundException("Category not found for this id!!"));

        if(categoryRepo.existsByParentCategory(category)){
            throw new GenericMessageException("This category is not a leaf node category");
        }

        Product product = new Product();
        product.setBrand(productDTO.getBrand());
        product.setCancellable(productDTO.isCancelable());;
        product.setName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setSeller(seller);
        product.setReturnable(productDTO.isReturnable());
        product.setCategory(category);

        productRepo.save(product);
        emailService.sendMail(adminEmail,"Product Added","Product is added by this seller "+seller.getEmail()+"and the product information is"+product);
    }

//    public void addProductVariation(){
//
//    }

    public Product viewProduct(Long productId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("User not found!!"));
        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourcesNotFoundException("Id doesn't exist!!"));
        if(product.isDeleted()){
            throw new GenericMessageException("This product is not available or deleted!!");
        }
        if(!Objects.equals(seller.getUserId(), product.getSeller().getUserId())){
            throw new GenericMessageException("You have not created any product!!");
        }
        return product;
    }

    public List<Product> viewAllProducts(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
       Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->new ResourcesNotFoundException("User not found!!"));

        List<Product> productList = new ArrayList<>();
        for(Product products: productList){
            if(products.getSeller().getEmail()==email){
                productList.add(products);
            }
            throw new GenericMessageException("you have not created any product!!");
        }
        return productList;
    }

    public void deleteProduct(Long productId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->new ResourcesNotFoundException("User not found!!"));

        Product product = productRepo.findById(productId).orElseThrow(()->new ResourcesNotFoundException("Product not found for this id!!"));

        if(!Objects.equals(product.getSeller().getUserId(), seller.getUserId())){
            throw new GenericMessageException("You have not created this product!!");
        }
        productRepo.deleteById(productId);
    }


}

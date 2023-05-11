package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.ProductDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ProductUpdateDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ProductVariationDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Product;
import com.ttn.bootcamp.project.bootcampproject.entity.product.ProductVariation;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.ProductRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.ProductVariationRepo;
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
    @Autowired
    ProductVariationRepo productVariationRepo;

    @Value("${admin.email}")
    String adminEmail;


    public void addProduct(ProductDTO productDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->new ResourcesNotFoundException("Seller not found!!"));

        Category category = categoryRepo.findById(productDTO.getCategoryId()).orElseThrow(()->new ResourcesNotFoundException("Category not found for this id!!"));

        if(categoryRepo.existsByParentCategory(category)){
            throw new GenericMessageException("This category is not a leaf node category");
        }

        if(!productRepo.existsByNameAndBrandAndCategoryAndSeller(productDTO.getProductName(), productDTO.getBrand(),category,seller)) {
            Product product = new Product();
            product.setBrand(productDTO.getBrand());
            product.setCancellable(productDTO.isCancelable());
            ;
            product.setName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setSeller(seller);
            product.setReturnable(productDTO.isReturnable());
            product.setCategory(category);

            productRepo.save(product);
            emailService.sendMail(adminEmail, "Product Added", "Product is added by this seller " + seller.getEmail() + "and the product information is" + product);
        }
        throw new GenericMessageException("Product is already exists!!");
    }


    public void addProductVariation(ProductVariationDTO productVariationDTO){
        Product product = productRepo.findById(productVariationDTO.getProductId())
                .orElseThrow(()->new GenericMessageException("Product id not found!!"));
        if(productVariationDTO.getQuantity()<0){
            throw new GenericMessageException("Quantity can't be less than 0");
        }

        if(productVariationDTO.getPrice()<0){
            throw new GenericMessageException("Price can't be less than 0");
        }
        if(product.isDeleted() && !product.isActive()){
            throw new GenericMessageException("Product is not activated!!");
        }
        ProductVariation productVariation = new ProductVariation();
        productVariation.setProduct(product);
        productVariation.setMetaData(productVariation.getMetaData());
        productVariation.setQuantityAvailable(productVariationDTO.getQuantity());
        productVariation.setPrice(productVariationDTO.getPrice());
        productVariationRepo.save(productVariation);
    }

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

    public void updateProduct(Long id, ProductUpdateDTO productUpdateDTO){
        Product product = productRepo.findById(id).orElseThrow(()-> new ResourcesNotFoundException("product not found for this id!!"));

        if(productUpdateDTO.getProductName() != null){
            product.setName(productUpdateDTO.getProductName());
        }
        product.setCancellable(productUpdateDTO.isCancel());
        product.setReturnable(productUpdateDTO.isReturn());
        if(productUpdateDTO.getDescription()!=null){
            product.setDescription(productUpdateDTO.getDescription());
        }
        productRepo.save(product);
    }

    public void viewProductByCustomter(Long productId){
        Product product = productRepo.findById(productId).orElseThrow(()->new ResourcesNotFoundException("No product found for this id!!"));

        if(product.isDeleted()){
            throw new GenericMessageException("This product is not available or deleted!!");
        }

    }

    public boolean activateProduct(Long id){
        Product product=productRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("No product found for this id!!"));

        String email = product.getSeller().getEmail();
        if(product.isDeleted()){
            throw new GenericMessageException("Product is deleted!!");
        }
        if(!product.isActive()){
            product.setActive(true);
            productRepo.save(product);
            emailService.sendMail(email,"Product Activation Status","Your product has been activated now!");
            return true;
        }
        return false;
    }

    public boolean deactivateProduct(Long id){
        Product product=productRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("No product found for this id!!"));

        String email = product.getSeller().getEmail();
        if(product.isDeleted()){
            throw new GenericMessageException("Product is deleted!!");
        }
        if(product.isActive()){
            product.setActive(false);
            productRepo.save(product);
            emailService.sendMail(email,"Product Activation Status","Your product has been deactivated now!");
            return true;
        }
        return false;
    }


}

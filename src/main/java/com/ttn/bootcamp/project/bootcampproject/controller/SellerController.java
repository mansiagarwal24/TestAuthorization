package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.repository.ProductVariationRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.service.CategoryService;
import com.ttn.bootcamp.project.bootcampproject.service.I18Service;
import com.ttn.bootcamp.project.bootcampproject.service.ProductService;
import com.ttn.bootcamp.project.bootcampproject.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    I18Service i18Service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SellerDTO sellerDTO){
        sellerService.createSeller(sellerDTO);
        return new ResponseEntity<>(i18Service.getMsg("seller.register"),HttpStatus.OK);
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewSellerProfile(){
        return new ResponseEntity<>(sellerService.viewProfile(),HttpStatus.OK);
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateSellerProfile(@Valid @ModelAttribute SellerUpdateDTO sellerUpdateDTO) throws IOException {
        sellerService.updateProfile(sellerUpdateDTO);
        return new ResponseEntity<>("Update Successfully!!",HttpStatus.OK);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateSellerPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO){
        sellerService.updatePassword(resetPasswordDTO);
        return new ResponseEntity<>("Password Update Successfully!!",HttpStatus.OK);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateSellerAddress(@RequestParam Long id,@RequestBody AddressDTO addressDTO){
        sellerService.updateAddress(id,addressDTO);
        return new ResponseEntity<>("Address Updated Successfully!!",HttpStatus.OK);

    }

    //CATEGORY API

    @GetMapping("/viewAllCategories")
    public ResponseEntity<?> viewAllCategories(){
        return new ResponseEntity<>( categoryService.getAllCategoriesForSeller(),HttpStatus.OK);
    }




    //PRODUCT API

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductDTO productDTO){
        productService.addProduct(productDTO);
        return new ResponseEntity<>("Product added Successfully!!",HttpStatus.OK);
    }
    @PostMapping("/addProductVariation")
    public ResponseEntity<?> addProductVariation(@Valid @ModelAttribute ProductVariationDTO productVariationDTO,@RequestPart("MetaData") String metadata){
        productService.addProductVariation(productVariationDTO,metadata);
        return new ResponseEntity<>("Product Variation added Successfully!!",HttpStatus.OK);
    }

    @GetMapping("/viewProductVariation")
    public ResponseEntity<?> viewProductVariation(@RequestParam Long id){
        return new ResponseEntity<>(productService.viewProductVariation(id),HttpStatus.OK);
    }
    @GetMapping("/viewAllProductVariation")
    public ResponseEntity<?> viewAllProductVariation(@RequestParam int offSet, @RequestParam int pageSize, @RequestParam Sort.Direction orderBy,@RequestParam String sortBy){
        return new ResponseEntity<>(productService.viewAllProductVariations(offSet,pageSize,orderBy,sortBy),HttpStatus.OK);
    }

    @GetMapping("/viewProduct")
    public ResponseEntity<?> viewProduct(@RequestParam Long productId){
        return new ResponseEntity<>(productService.viewProduct(productId),HttpStatus.OK);
    }

    @GetMapping("/viewAllProduct")
    public ResponseEntity<?> viewAllProduct(@RequestParam int offSet, @RequestParam int pageSize, @RequestParam Sort.Direction orderBy,@RequestParam String sortBy){
        return new ResponseEntity<>(productService.viewAllProducts(offSet,pageSize,orderBy,sortBy),HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted Successfully!!",HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id,@RequestBody ProductUpdateDTO productUpdateDTO){
        productService.updateProduct(id,productUpdateDTO);
        return new ResponseEntity<>("Product updated Successfully!!",HttpStatus.OK);
    }

    @PutMapping("/updateProductVariation")
    public ResponseEntity<?> updateProductVariation(@RequestParam Long id,@ModelAttribute ProductVariationDTO productVariationDTO,@RequestPart("metadata") String metadata) throws IOException {
        productService.updateProductVariation(id,productVariationDTO,metadata);
        return new ResponseEntity<>("Product Variation updated Successfully!!",HttpStatus.OK);
    }


}

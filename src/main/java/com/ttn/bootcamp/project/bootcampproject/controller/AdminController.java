package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import com.ttn.bootcamp.project.bootcampproject.service.AdminService;
import com.ttn.bootcamp.project.bootcampproject.service.CategoryService;
import com.ttn.bootcamp.project.bootcampproject.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Retention;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;


    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(@RequestParam int pageOffSet,@RequestParam int pageSize,@RequestParam String sort){
        List<CustomerResponseDTO> customerList = adminService.findAllCustomer(pageOffSet, pageSize, sort);
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getSeller(@RequestParam(defaultValue = "0") int pageOffSet,@RequestParam(defaultValue = "10") int pageSize,@RequestParam String sort){
        List<SellerResponseDTO> sellerList = adminService.findAllSeller(pageOffSet, pageSize, sort);
        return new ResponseEntity<>(sellerList, HttpStatus.OK);
    }

    @PutMapping("/activate-user/{id}")
    public ResponseEntity<?> activateUser(@PathVariable Long id){
        if(adminService.activateUser(id) ){
            return new ResponseEntity<>("Account has been activated!!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Account is already activated", HttpStatus.OK);
    }

    @PutMapping("/deactivate-user/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id){
        if(adminService.deactivateUser(id)) {
            return new ResponseEntity<>("Account has been deactivated!!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Account is already deactivated", HttpStatus.OK);
    }



     //CATEGORY API

    @PostMapping("/addMetaDataField")
    public ResponseEntity<?> addMetadataField(@Valid @RequestBody CategoryMetadataField categoryMetadataField){
        categoryService.addMetadataField(categoryMetadataField);
        return new ResponseEntity<>("category field name is added!!"+categoryMetadataField,HttpStatus.OK);

    }

    @GetMapping("/getMetadataField")
    public ResponseEntity<?> getMetadataField(@RequestParam int offSet,@RequestParam int size,@RequestParam Sort.Direction orderBy,@RequestParam String sortBy){
        List<CategoryMetadataResponseDTO> metaDataList=categoryService.getMetadataField(offSet,size,orderBy,sortBy);
        return new ResponseEntity<>(metaDataList,HttpStatus.OK);
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestParam String categoryName,@RequestParam(required = false) Long parentId){
        categoryService.addCategory(categoryName,parentId);
        return new ResponseEntity<>("Category Added Successfully!!",HttpStatus.OK);
    }

    @GetMapping("/viewCategory")
    public ResponseEntity<?> viewCategory(@RequestParam Long id){
        return new ResponseEntity<>(categoryService.viewCategory(id),HttpStatus.OK);
    }

    @GetMapping("/viewAllCategory")
    public ResponseEntity<?> viewCategory(@RequestParam int offSet, @RequestParam int size, @RequestParam Sort.Direction orderBy,@RequestParam String sortBy){
        return new ResponseEntity<>(categoryService.viewAllCategories(offSet,size,orderBy,sortBy),HttpStatus.OK);
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestParam Long id,@Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO){
        categoryService.updateCategory(id,categoryUpdateDTO);
        return new ResponseEntity<>("category updated successfully!!",HttpStatus.OK);
    }

    @PostMapping("/add-metadata-field-values")
    public ResponseEntity<?> addMetaDataValues(@RequestBody @Valid MetadataFieldValuesDTO metadataFieldValuesDTO){
        categoryService.addMetadataFieldValues(metadataFieldValuesDTO);
        return new ResponseEntity<>("MetaData field values added!!",HttpStatus.OK);
    }

    @PutMapping("/update-metadata-field-values")
    public ResponseEntity<?> updateMetaDataValues(@RequestBody @Valid MetadataFieldValuesDTO metadataFieldValuesDTO){
        categoryService.updateMetadataFieldValues(metadataFieldValuesDTO);
        return new ResponseEntity<>("MetaData field values are updated!!",HttpStatus.OK);
    }

    //PRODUCT API

    @PutMapping("/activateProduct")
    public ResponseEntity<?> activateProduct(@RequestParam Long id){
        if(productService.activateProduct(id)){
            return new ResponseEntity<>("Product has been activated!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Product is already activated!!",HttpStatus.OK);
    }
    @PutMapping("/deactivateProduct")
    public ResponseEntity<?> deactivateProduct(@RequestParam Long id){
        if(productService.deactivateProduct(id)){
            return new ResponseEntity<>("Product has been deactivated!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Product is already deactivated!!",HttpStatus.OK);
    }


    @GetMapping("/viewProductById")
    public ResponseEntity<?> viewProductById(@RequestParam Long id){
        return new ResponseEntity<>(productService.viewProductByAdmin(id),HttpStatus.OK);

    }

    @GetMapping("/viewAllProducts")
    public ResponseEntity<?> viewAllProducts(@RequestParam int offSet, @RequestParam int size, @RequestParam Sort.Direction orderBy,@RequestParam String sortBy){
        return new ResponseEntity<>(productService.viewAllProductsByAdmin(offSet, size, orderBy, sortBy),HttpStatus.OK);

    }
}

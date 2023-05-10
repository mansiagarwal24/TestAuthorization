package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.CategoryUpdateDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.CustomerResponseDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.MetadataFieldValuesDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerResponseDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import com.ttn.bootcamp.project.bootcampproject.service.AdminService;
import com.ttn.bootcamp.project.bootcampproject.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    CategoryService categoryService;


    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(@RequestParam int pageOffSet,@RequestParam int pageSize,@RequestParam String sort){
        List<CustomerResponseDTO> customerList = adminService.findAllCustomer(pageOffSet, pageSize, sort);
        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getSeller(@RequestParam(defaultValue = "10",required = false) int pageOffSet,@RequestParam int pageSize,@RequestParam String sort){
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
        return categoryService.addMetadataField(categoryMetadataField);
    }

    @GetMapping("/getMetadataField")
    public ResponseEntity<?> getMetadataField(@RequestParam int offSet,@RequestParam int size,@RequestParam Sort.Direction orderBy,@RequestParam String sortBy){
        return categoryService.getMetadataField(offSet,size,orderBy,sortBy);
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestParam String categoryName,@RequestParam(required = false) Long parentId){
        return categoryService.addCategory(categoryName,parentId);
    }

    @GetMapping("/viewCategory")
    public ResponseEntity<?> viewCategory(@RequestParam Long id){
        return categoryService.viewCategory(id);
    }

    @GetMapping("/viewAllCategory")
    public ResponseEntity<?> viewCategory(@RequestParam int offSet, @RequestParam int size, @RequestParam Sort.Direction orderBy,@RequestParam String sortBy){
        return categoryService.viewAllCategories(offSet,size,orderBy,sortBy);
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestParam Long id,@RequestBody CategoryUpdateDTO categoryUpdateDTO){
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
}

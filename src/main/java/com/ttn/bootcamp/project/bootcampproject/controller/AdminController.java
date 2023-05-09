package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.CategoryUpdateDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import com.ttn.bootcamp.project.bootcampproject.service.AdminService;
import com.ttn.bootcamp.project.bootcampproject.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @Autowired
    CategoryService categoryService;


    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(@RequestParam int pageOffSet,@RequestParam int pageSize,@RequestParam String sort){
        return adminService.findAllCustomer(pageOffSet,pageSize,sort);
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getSeller(@RequestParam(defaultValue = "10",required = false) int pageOffSet,@RequestParam int pageSize,@RequestParam String sort){
        return adminService.findAllSeller(pageOffSet,pageSize,sort);

    }

    @PutMapping("/activate-user/{id}")
    public ResponseEntity<?> activateUser(@PathVariable Long id){
        return adminService.activateUser(id);
    }

    @PutMapping("/deactivate-user/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id){
       return adminService.deactivateUser(id);
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
        return categoryService.updateCategory(id,categoryUpdateDTO);
    }

//    @PostMapping("/addMetaDataFieldValues")
//    public ResponseEntity<?> addMetaDataValues(@RequestBody CategoryMetadataFieldValues categoryMetadataFieldValues){
//        return categoryService.addMetadataFieldValues(categoryMetadataFieldValues);
//    }
}

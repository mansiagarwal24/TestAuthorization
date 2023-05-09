package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.CategoryMetadataResponse;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryMetadataFieldRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMetadataFieldRepo categoryMetadataFieldRepo;
    @Autowired
    CategoryRepo categoryRepo;

    public ResponseEntity<?> addMetadataField(CategoryMetadataField categoryMetadataField){
        if(categoryMetadataFieldRepo.existsByFieldName(categoryMetadataField.getFieldName())){
            return new ResponseEntity<>("Name is already registered!!", HttpStatus.BAD_REQUEST);
        }

        categoryMetadataField.setFieldName(categoryMetadataField.getFieldName());
        categoryMetadataFieldRepo.save(categoryMetadataField);
        return new ResponseEntity<>("category field name is added!!"+categoryMetadataField,HttpStatus.OK);
    }

    public ResponseEntity<?> getMetadataField(int offSet, int size, Sort.Direction orderBy, String sortBy){
        PageRequest page = PageRequest.of(offSet,size,orderBy,sortBy);
        Page<CategoryMetadataField> categoryMetadataFieldPage = categoryMetadataFieldRepo.findAll(page);
        List<CategoryMetadataResponse> metaDataList=new ArrayList<>();
        for(CategoryMetadataField list:categoryMetadataFieldPage) {
            CategoryMetadataResponse categoryMetadataResponse = new CategoryMetadataResponse();
            categoryMetadataResponse.setId(list.getId());
            categoryMetadataResponse.setFieldName(list.getFieldName());
            metaDataList.add(categoryMetadataResponse);
        }
        return new ResponseEntity<>(metaDataList,HttpStatus.OK);
    }

    public ResponseEntity<?> addCategory(String categoryName,Long parentId){
        if(categoryRepo.existsByName(categoryName)){
            return new ResponseEntity<>("This category is already defined",HttpStatus.UNAUTHORIZED);
        }
        Category category = new Category();
        category.setName(categoryName);
        if(parentId != null){
            Category parentCategory = categoryRepo.findById(parentId).orElseThrow(()-> new RuntimeException("Parent id is not valid"));
            category.setParentCategory(parentCategory);
        }
        categoryRepo.save(category);
        return new ResponseEntity<>("Category Added Successfully!!",HttpStatus.OK);
    }

    public ResponseEntity<?> viewCategory(Long categoryId){
        Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourcesNotFoundException("Id not found!!"));
        List<Category> categoryList = new ArrayList<>();
        Category parentCategory = category.getParentCategory();
        if(parentCategory != null){
            Category categories = categoryRepo.findById(parentCategory.getId()).orElseThrow(()-> new ResourcesNotFoundException("parent category not found!!"));
            categoryList.add(categories);
        }
        categoryList.add(category);
        return new ResponseEntity<>(categoryList,HttpStatus.OK);
    }

    public ResponseEntity<?> viewAllCategories(int offSet, int size, Sort.Direction orderBy,String sortBy){
        PageRequest page = PageRequest.of(offSet,size,orderBy,sortBy);
        Page<Category> categoryPages = categoryRepo.findAll(page);
        List<Category> categoryList=new ArrayList<>();
        for(Category category:categoryPages){
            Long id=category.getId();
            Category parentCategory = categoryRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("parent Category not found!!"));
            if(parentCategory!=null){
                categoryList.add(category);
            }
        }
        return new ResponseEntity<>(categoryList,HttpStatus.OK);
    }

}

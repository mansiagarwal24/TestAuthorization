package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.CategoryMetadataResponse;
import com.ttn.bootcamp.project.bootcampproject.dto.CategoryUpdateDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.MetadataFieldValuesDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.CategoryMetaDataId;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryMetadataFieldRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.MetaDataValuesRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMetadataFieldRepo categoryMetadataFieldRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    MetaDataValuesRepo metaDataValuesRepo;

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
//        Category parentCategory = category.getParentCategory();
//        if(parentCategory != null){
//            Category categories = categoryRepo.findById(parentCategory.getId()).orElseThrow(()-> new ResourcesNotFoundException("parent category not found!!"));
//            categoryList.add(categories);
//        }
        Category child = categoryRepo.findByParentCategory(category);
        categoryList.add(child);
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

    public void  updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO){
        Category category = categoryRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("Id is not valid"));
        if(categoryRepo.existsByName(categoryUpdateDTO.getCategoryName())){
            throw new GenericMessageException("Category Name is already exist!!");
        }
        Category parentCategory=categoryRepo.findById(categoryUpdateDTO.getParentId()).orElseThrow(()->new ResourcesNotFoundException("No parent exist for this id!!"));
        category.setName(category.getName());
        category.setParentCategory(parentCategory);
        categoryRepo.save(category);
    }

    public void addMetadataFieldValues(MetadataFieldValuesDTO metadataFieldValuesDTO){
        Long categoryId = metadataFieldValuesDTO.getCategoryId();
        Category category=categoryRepo.findById(categoryId).orElseThrow(()->new ResourcesNotFoundException("Category Id not Found"));

        Long categoryMetadataFieldId = metadataFieldValuesDTO.getCategoryMetaDataFieldId();
        CategoryMetadataField categoryMetadataField=categoryMetadataFieldRepo.findById(categoryMetadataFieldId).orElseThrow(()->new ResourcesNotFoundException("Category MetadataId not found."));

        CategoryMetadataFieldValues metadataFieldValues = new CategoryMetadataFieldValues();
        CategoryMetaDataId id = new CategoryMetaDataId();
        id.setCategoryId(categoryId);
        id.setCategoryMetadataFieldId(categoryMetadataFieldId);

        if(metaDataValuesRepo.existsById(id)) {
            throw new GenericMessageException("ids already exist!!");
        }

        metadataFieldValues.setCategoryMetadataId(id);
        metadataFieldValues.setCategoryMetadataField(categoryMetadataField);
        metadataFieldValues.setCategory(category);
        metadataFieldValues.setValue(metadataFieldValuesDTO.getValues());

        metaDataValuesRepo.save(metadataFieldValues);
    }

    public void updateMetadataFieldValues(MetadataFieldValuesDTO metadataFieldValuesDTO){
        Category category = categoryRepo.findById(metadataFieldValuesDTO.getCategoryId())
                .orElseThrow(()->new ResourcesNotFoundException("Category Id not Found"));

        CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepo.findById(metadataFieldValuesDTO.getCategoryMetaDataFieldId())
                .orElseThrow(()->new ResourcesNotFoundException("Category Metadata field Id not Found"));

        CategoryMetaDataId id = new CategoryMetaDataId();
        id.setCategoryId(category.getId());
        id.setCategoryMetadataFieldId(categoryMetadataField.getId());

        if(!metaDataValuesRepo.existsById(id)) {
            throw new GenericMessageException("Ids not exist!!");
        }
        CategoryMetadataFieldValues metadataFieldValues = new CategoryMetadataFieldValues();
        metadataFieldValues.setValue(metadataFieldValuesDTO.getValues());
        metaDataValuesRepo.save(metadataFieldValues);
        }


}

package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.CategoryMetaDataId;
import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataField;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryMetadataFieldRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.CategoryRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.MetaDataValuesRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    CategoryMetadataFieldRepo categoryMetadataFieldRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    MetaDataValuesRepo metaDataValuesRepo;

    public void addMetadataField(CategoryMetadataField categoryMetadataField){
        if(categoryMetadataFieldRepo.existsByFieldName(categoryMetadataField.getFieldName())){
            throw new GenericMessageException("Name is already registered!!");
        }

        categoryMetadataField.setFieldName(categoryMetadataField.getFieldName());
        categoryMetadataFieldRepo.save(categoryMetadataField);
    }

    public List<CategoryMetadataResponseDTO> getMetadataField(int offSet, int size, Sort.Direction orderBy, String sortBy){
        PageRequest page = PageRequest.of(offSet,size,orderBy,sortBy);
        Page<CategoryMetadataField> categoryMetadataFieldPage = categoryMetadataFieldRepo.findAll(page);
        List<CategoryMetadataResponseDTO> metaDataList=new ArrayList<>();

        for(CategoryMetadataField list:categoryMetadataFieldPage) {
            CategoryMetadataResponseDTO categoryMetadataResponse = new CategoryMetadataResponseDTO();
            categoryMetadataResponse.setId(list.getId());
            categoryMetadataResponse.setFieldName(list.getFieldName());
            metaDataList.add(categoryMetadataResponse);
        }
        return metaDataList;
    }

    public Category addCategory(String categoryName,Long parentId){
        if(categoryRepo.existsByName(categoryName)){
            throw new GenericMessageException("Category Name already exists!!");
        }
        Category category = new Category();
        if(parentId == 0 ){
            category.setName(categoryName);
            categoryRepo.save(category);
            return category;
        }
        if(parentId!=null){
            Category parentCategory = categoryRepo.findById(parentId).orElseThrow(()->new ResourcesNotFoundException("Id not exist!!"));
            category.setParentCategory(parentCategory);
        }
        Category category1=categoryRepo.findById(parentId).get();
        if(parentId != null){
            List<Category> categoryList = categoryRepo.findByParentCategory(category1);
            for(Category c: categoryList){
                if(c.getName().equals(categoryName)){
                    throw new GenericMessageException("This category is already exists here!!");
                }
            }
            while (category1!=null){
                if(category1.getName().equals(categoryName)){
                    throw new GenericMessageException("This category is already exists here!!");
                }
                category1=category1.getParentCategory();
            }
        }
        category.setName(categoryName);
        categoryRepo.save(category);
        return category;
    }

    public CategoryResponseDTO  viewCategory(Long categoryId){
        Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourcesNotFoundException("Id not found!!"));
        List<Category> childCategory = categoryRepo.findByParentCategory(category);
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setParentCategory(category.getParentCategory());
        categoryResponseDTO.setChildCategory(childCategory);
        return categoryResponseDTO;
    }

    public List<CategoryResponseDTO> viewAllCategories(int offSet, int size, Sort.Direction orderBy,String sortBy){
        PageRequest page = PageRequest.of(offSet,size,orderBy,sortBy);
        Page<Category> categoryPages = categoryRepo.findAll(page);
        List<CategoryResponseDTO> categoryList=new ArrayList<>();

        for(Category category:categoryPages){
            CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
            List<Category> childCategory = categoryRepo.findByParentCategory(category);
            categoryResponseDTO.setId(category.getId());
            categoryResponseDTO.setName(category.getName());
            categoryResponseDTO.setParentCategory(category.getParentCategory());
            categoryResponseDTO.setChildCategory(childCategory);
            categoryList.add(categoryResponseDTO);
        }
        return categoryList;
    }

    public void updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO){
        Category category = categoryRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("Id is not valid"));

        if(categoryRepo.existsByName(categoryUpdateDTO.getCategoryName())){
            throw new GenericMessageException("Category Name is already exist!!");
        }
        category.setName(categoryUpdateDTO.getCategoryName());
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
            throw new GenericMessageException("values for these ids are already exist!!");
        }

        metadataFieldValues.setCategoryMetadataId(id);
        metadataFieldValues.setCategoryMetadataField(categoryMetadataField);
        metadataFieldValues.setCategory(category);

        metadataFieldValues.setValue(metadataFieldValuesDTO.getValues().stream().toList());

        metaDataValuesRepo.save(metadataFieldValues);
    }

    public void updateMetadataFieldValues(MetadataFieldValuesDTO metadataFieldValuesDTO){
        CategoryMetaDataId categoryMetaDataId=new CategoryMetaDataId();
        categoryMetaDataId.setCategoryId(metadataFieldValuesDTO.getCategoryId());
        categoryMetaDataId.setCategoryMetadataFieldId(metadataFieldValuesDTO.getCategoryMetaDataFieldId());
        CategoryMetadataFieldValues metadataFieldValues=metaDataValuesRepo.findById(categoryMetaDataId).
                orElseThrow(()->new ResourcesNotFoundException("Category metadata values not found."));

        Set<String> values=new HashSet<>();
        values.addAll(metadataFieldValues.getValue());
        values.addAll(metadataFieldValuesDTO.getValues());
        metadataFieldValues.setValue(  values.stream().toList());

        metaDataValuesRepo.save(metadataFieldValues);
    }

    public List<Category> getAllCategoriesForSeller(){
        List<Category> categories = categoryRepo.findAll();
        List<Category> categoryList=new ArrayList<>();
        for(Category category:categories){
            if(!categoryRepo.existsByParentCategory(category)){
                categoryList.add(category);
            }
        }
        return categoryList;
    }

    public List<ViewCategoryDTO> viewCategoryForCustomer(Long id){
        Category parentCategory = categoryRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("Category id not found!!"));
        List<Category> childCategory=categoryRepo.findByParentCategory(parentCategory);
        List<ViewCategoryDTO> categoryList = new ArrayList<>();

        for(Category category:childCategory) {
            ViewCategoryDTO viewCategoryDTO = new ViewCategoryDTO();
            viewCategoryDTO.setId(category.getId());
            viewCategoryDTO.setCategoryName(category.getName());
            viewCategoryDTO.setParentCategoryId(category.getParentCategory());

            categoryList.add(viewCategoryDTO);
        }
        return categoryList;
    }
}

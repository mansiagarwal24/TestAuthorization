package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.product.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    @Autowired
    CategoryMetadataFieldRepo categoryMetadataFieldRepo;
    @Autowired
    MetaDataValuesRepo metaDataValuesRepo;

    @Value("${admin.email}")
    String adminEmail;


    public void addProduct(ProductDTO productDTO){
        if(Objects.isNull(productDTO)){
            throw new GenericMessageException("Invalid Input parameter!!");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->new ResourcesNotFoundException("Seller not found!!"));

        Category category = categoryRepo.findById(productDTO.getCategoryId()).orElseThrow(()->new ResourcesNotFoundException("Category not found for this id!!"));

        if(categoryRepo.existsByParentCategory(category)){
            throw new GenericMessageException("This category is not a leaf node category");
        }

        if(productRepo.existsByBrandAndCategoryAndSeller( productDTO.getBrand(),category,seller)) {
            throw new GenericMessageException("Product is already exists!!");
        }
            Product product = new Product();
            product.setBrand(productDTO.getBrand());
            product.setCancellable(productDTO.isCancelable());
            product.setName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setSeller(seller);
            product.setReturnable(productDTO.isReturnable());
            product.setCategory(category);

            productRepo.save(product);
            emailService.sendMail(adminEmail, " New Product Added", "Product is added by this  " + seller.getEmail() + " Seller and the product Id is" + product.getId());
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
        if(!product.isActive() || product.isDeleted()){
            throw new GenericMessageException("Product is not activated or deleted!!");
        }

        Map<String,String> metadata=productVariationDTO.getMetadataValues();
        Map<String,String> data = new HashMap<>();
        for(Map.Entry<String,String> map: metadata.entrySet()){
            if(!categoryMetadataFieldRepo.existsByFieldName(map.getKey())){
                throw new GenericMessageException("Field Values doesn't exist!!");
            }
            CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepo.findByFieldName(map.getKey());

            if(!metaDataValuesRepo.existsByCategoryAndCategoryMetadataField(product.getCategory(),categoryMetadataField)){
                throw new GenericMessageException("Field Values doesn't exist!!");
            }
            CategoryMetadataFieldValues categoryMetadataFieldValues = metaDataValuesRepo.findByCategoryAndCategoryMetadataField(
                    product.getCategory(),categoryMetadataField);
            List<String> strings=categoryMetadataFieldValues.getValue().stream().toList();

            for(String s:strings){
                if(s.equals(map.getValue())){
                    data.put(map.getKey(),s);
                    break;
                }
            }
        }
        ProductVariation productVariation = new ProductVariation();
        productVariation.setProduct(product);
        productVariation.setMetaData(data);
        productVariation.setQuantityAvailable(productVariationDTO.getQuantity());
        productVariation.setPrice(productVariationDTO.getPrice());
        productVariationRepo.save(productVariation);
    }

    public ProductDTO viewProduct(Long productId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("User not found!!"));
        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourcesNotFoundException("Id doesn't exist!!"));
        if(product.isDeleted()){
            throw new GenericMessageException("This product is not available or deleted!!");
        }
        if(!Objects.equals(seller.getUserId(), product.getSeller().getUserId())){
            throw new GenericMessageException("You have not created this product!!");
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(product.getName());
        productDTO.setBrand(product.getBrand());
        productDTO.setReturnable(product.isReturnable());
        productDTO.setCancelable(product.isCancellable());
        productDTO.setCategoryId(product.getCategory().getId());
        return productDTO;
    }

    public List<ProductDTO> viewAllProducts(int offSet, int size, Sort.Direction orderBy,String sortBy){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->new ResourcesNotFoundException("User not found!!"));
//        List<Product> products = productRepo.findAllBySeller(seller);
        PageRequest page = PageRequest.of(offSet,size,orderBy,sortBy);
        Page<Product> productPages = productRepo.findAll(page);
        List<ProductDTO>productList=new ArrayList<>();
        for(Product product:productPages){
            if(product.getSeller()==seller) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setProductName(product.getName());
                productDTO.setDescription(product.getDescription());
                productDTO.setBrand(product.getBrand());
                productDTO.setCategoryId(product.getCategory().getId());
                productDTO.setCancelable(product.isCancellable());
                productList.add(productDTO);
            }
        }
        return productList;


//        for(Product product: productList){
//            if(Objects.equals(product.getSeller().getEmail(),email)){
//                productList.add(product);
//            }
//            throw new GenericMessageException("you have not created any product!!");
//        }
    }

    public ViewProductVariationDTO viewProductVariation(Long id){
        if(!productVariationRepo.existsById(id)){
            throw new GenericMessageException("Id not exists!!");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).get();
        ProductVariation productVariation = productVariationRepo.findById(id).get();

        Product product = productRepo.findById(productVariation.getProduct().getId())
                .orElseThrow(()->new GenericMessageException("Product id not found!!"));

        if(product.isDeleted()){
            throw new GenericMessageException("This product is not available or deleted!!");
        }
        if(!Objects.equals(product.getSeller(),seller)){
            throw new GenericMessageException("your have not created this product!!");
        }
        ViewProductVariationDTO productVariationDTO = new ViewProductVariationDTO();
        productVariationDTO.setProductId(productVariation.getProduct().getId());
        productVariationDTO.setQuantity(productVariation.getQuantityAvailable());
        productVariationDTO.setActive(productVariation.isActive());
        productVariationDTO.setPrice(productVariation.getPrice());
        productVariationDTO.setMetadataValues(productVariation.getMetaData());
        productVariationDTO.setProductName(product.getName());
        productVariationDTO.setDescription(product.getDescription());
        productVariationDTO.setBrand(product.getBrand());
        return productVariationDTO;
    }

    public List<ProductVariationDTO> viewAllProductVariations(int offSet, int size, Sort.Direction orderBy,String sortBy) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(() -> new ResourcesNotFoundException("User not found!!"));

        PageRequest page = PageRequest.of(offSet, size, orderBy, sortBy);
        Page<ProductVariation> productVariationPages = productVariationRepo.findAll(page);
        List<ProductVariationDTO> productVariations = new ArrayList<>();
        for (ProductVariation productVariation : productVariationPages) {
            if(productVariation.getProduct().getSeller()==seller) {
                ProductVariationDTO productVariation1 = new ProductVariationDTO();
                productVariation1.setProductId(productVariation.getProduct().getId());
                productVariation1.setPrice(productVariation.getPrice());
                productVariation1.setMetadataValues(productVariation.getMetaData());
                productVariation1.setQuantity(productVariation.getQuantityAvailable());
                productVariations.add(productVariation1);
            }
        }
        return productVariations;
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

        if(productRepo.existsByBrandAndCategoryAndSeller( productUpdateDTO.getBrand(),product.getCategory(),product.getSeller())) {
            throw new GenericMessageException("Product is already exists!!");
        }

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

    private String basePath="/home/mansi/Downloads/bootcamp-project/images/";
    public void updateProductVariation(Long id,ProductVariationDTO productVariationDTO) throws IOException {
        if(!productVariationRepo.existsById(id)){
            throw new GenericMessageException("Id not exists!!");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).get();
        ProductVariation productVariation = productVariationRepo.findById(id).get();

        Product product = productRepo.findById(productVariation.getProduct().getId())
                .orElseThrow(()->new GenericMessageException("Product id not found!!"));

        if(!Objects.equals(product.getSeller(),seller)){
            throw new GenericMessageException("your have not created this product!!");
        }

        if(product.isDeleted() || !product.isActive()){
            throw new GenericMessageException("Product is not activated or deleted!!");
        }

        Map<String,String> metadata=productVariation.getMetaData();
        Map<String,String> data = new HashMap<>();
        for(Map.Entry<String,String> map: metadata.entrySet()){
            if(!categoryMetadataFieldRepo.existsByFieldName(map.getKey())){
                throw new GenericMessageException("Field Values doesn't exist!!");
            }
            CategoryMetadataField categoryMetadataField = categoryMetadataFieldRepo.findByFieldName(map.getKey());

            if(!metaDataValuesRepo.existsByCategoryAndCategoryMetadataField(product.getCategory(),categoryMetadataField)){
                throw new GenericMessageException("Field Values doesn't exist!!");
            }
            CategoryMetadataFieldValues categoryMetadataFieldValues = metaDataValuesRepo.findByCategoryAndCategoryMetadataField(
                    product.getCategory(),categoryMetadataField);
            List<String> strings=categoryMetadataFieldValues.getValue().stream().toList();

            for(String s:strings){
                if(s.equals(map.getValue())){
                    data.put(map.getKey(),s);
                    break;
                }
            }
        }
        ProductVariation productVariations = new ProductVariation();
        productVariations.setMetaData(data);
        productVariations.setQuantityAvailable(productVariationDTO.getQuantity());
        productVariations.setPrice(productVariationDTO.getPrice());
        productVariations.setActive(productVariationDTO.isActive());

        productVariation.setPrimaryImageName(basePath+productVariationDTO.getImage().getOriginalFilename());
        productVariationRepo.save(productVariation);
        productVariationDTO.getImage().transferTo(new File(basePath+productVariationDTO.getImage().getOriginalFilename()));

    }

    public Product viewProductByCustomer(Long productId){
        Product product = productRepo.findById(productId).orElseThrow(()->new ResourcesNotFoundException("No product found for this id!!"));

        if(product.isDeleted() && !product.isActive()){
            throw new GenericMessageException("This product is not available or deleted!!");
        }
        return product;
    }

    public List<ProductDTO> viewAllProductByCustomer(Long id){
        Category category = categoryRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("Category Id not found!! "));

        if(categoryRepo.existsByParentCategory(category)){
            throw new GenericMessageException("This category is not a leaf node category");
        }

//        if(!productVariationRepo.existsByProduct(product)){
//            throw new GenericMessageException("there is no product variation for this category ");
//        }

        List<Product> productList=productRepo.findByCategory(category);
        List<ProductDTO> productDTOList=new ArrayList<>();

        for(Product product: productList){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductName(product.getName());
            productDTO.setBrand(product.getBrand());
            productDTO.setReturnable(product.isReturnable());
            productDTO.setDescription(product.getDescription());
            productDTO.setCategoryId(product.getCategory().getId());
            productDTO.setCancelable(product.isCancellable());
            productDTOList.add(productDTO);
        }
        return productDTOList;

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

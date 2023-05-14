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

    private String basePath="/home/mansi/Downloads/bootcamp-project/images/";


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
            product.setCancel(productDTO.isCancelable());
            product.setName(productDTO.getProductName());
            product.setDescription(productDTO.getDescription());
            product.setSeller(seller);
            product.setReturnable(productDTO.isReturnable());
            product.setCategory(category);

            productRepo.save(product);
            emailService.sendMail(adminEmail, " New Product Added", "Product is added by this  " + seller.getEmail() + " Seller and the product Id is" + product.getId());
    }


    public void addProductVariation(ProductVariationDTO productVariationDTO,String metaData){
        Product product = productRepo.findById(productVariationDTO.getProductId())
                .orElseThrow(()->new ResourcesNotFoundException("Product id not found!!"));
        if(productVariationDTO.getQuantity()<0){
            throw new GenericMessageException("Quantity can't be less than 0");
        }

        if(productVariationDTO.getPrice()<0){
            throw new GenericMessageException("Price can't be less than 0");
        }
        if(!product.getActive() || product.getDeleted()){
            throw new GenericMessageException("Product is not activated or deleted!!");
        }

//        String[] metadataPairs = metaData.split(",");
//        for (String pair : metadataPairs) {
//            String[] fieldAndValue = pair.split(":");
//            String field = fieldAndValue[0].replace("{"," ").trim();
//            String value = fieldAndValue[1].replaceAll("[^a-zA-Z0-9]"," ").trim();
//
//            if(!categoryMetadataFieldRepo.existsByFieldName(field)){
//                throw new GenericMessageException("Field Values doesn't exist!!");
//            }
//
//        }
        ProductVariation productVariation = new ProductVariation();
        productVariation.setProduct(product);
        productVariation.setQuantityAvailable(productVariationDTO.getQuantity());
        productVariation.setActive(true);
        productVariation.setPrice(productVariationDTO.getPrice());
        productVariation.setMetadata(metaData);

        productVariationRepo.save(productVariation);
    }

    public ProductDTO viewProduct(Long productId){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("User not found!!"));
        Product product = productRepo.findById(productId).orElseThrow(()-> new ResourcesNotFoundException("Id doesn't exist!!"));
        if(product.getDeleted()){
            throw new GenericMessageException("This product is not available or deleted!!");
        }
        if(!Objects.equals(seller.getUserId(), product.getSeller().getUserId())){
            throw new GenericMessageException("You have not created this product!!");
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(product.getName());
        productDTO.setBrand(product.getBrand());
        productDTO.setReturnable(product.getReturnable());
        productDTO.setCancelable(product.getCancel());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setCategoryName(product.getCategory().getName());
        productDTO.setDescription(product.getDescription());

        List<ProductVariation> productVariationList = productVariationRepo.findByProduct(product);
        List<ProductVariationDTO> productVariationDTOList = new ArrayList<>();
        for(ProductVariation productVariation: productVariationList ){
            ProductVariationDTO productVariationDTO = new ProductVariationDTO();
            productVariationDTO.setQuantity(productVariation.getQuantityAvailable());
            productVariationDTO.setPrice(productVariation.getPrice());
            productVariationDTOList.add(productVariationDTO);
        }

        productDTO.setProductVariation(productVariationDTOList);

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
                productDTO.setCancelable(product.getCancel());
                productDTO.setCategoryName(product.getCategory().getName());
                productDTO.setReturnable(product.getReturnable());
                productList.add(productDTO);
            }
        }
        return productList;
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

        if(product.getDeleted()){
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
        productVariationDTO.setMetadataValues(productVariation.getMetadata());
        productVariationDTO.setProductName(product.getName());
        productVariationDTO.setDescription(product.getDescription());
        productVariationDTO.setBrand(product.getBrand());
        productVariationDTO.setCategoryId(productVariation.getProduct().getCategory().getId());
        productVariationDTO.setCategoryName(productVariation.getProduct().getCategory().getName());
        productVariationDTO.setImageName(productVariation.getPrimaryImageName());
        productVariationDTO.setCategoryMetadataValues(productVariation.getProduct().getCategory().getCategoryMetadataFieldValues());

        return productVariationDTO;
    }

    public List<ProductVariationDTO> viewAllProductVariations(int offSet, int size, Sort.Direction orderBy,String sortBy) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(() -> new ResourcesNotFoundException("User not found!!"));

        PageRequest page = PageRequest.of(offSet, size, orderBy, sortBy);
        Page<ProductVariation> productVariationPages = productVariationRepo.findAll(page);
        List<ProductVariationDTO> productVariations = new ArrayList<>();
        for (ProductVariation productVariation : productVariationPages) {
            if(Objects.equals(productVariation.getProduct().getSeller(),seller)) {
                ProductVariationDTO productVariationDTO = new ProductVariationDTO();
                productVariationDTO.setProductId(productVariation.getProduct().getId());
                productVariationDTO.setPrice(productVariation.getPrice());
                productVariationDTO.setProductName(productVariation.getProduct().getName());
//                productVariation1.setMetadataValues(productVariation.getMetadata());
//                productVariationDTO.setImage(productVariation.getPrimaryImageName());
                productVariationDTO.setMetadataValues(productVariation.getMetadata());
                productVariationDTO.setQuantity(productVariation.getQuantityAvailable());
                productVariations.add(productVariationDTO);
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
        product.setDeleted(true);
        productRepo.save(product);
    }

    public void updateProduct(Long id, ProductUpdateDTO productUpdateDTO){
        Product product = productRepo.findById(id).orElseThrow(()-> new ResourcesNotFoundException("product not found for this id!!"));

        if(productRepo.existsByBrandAndCategoryAndSeller( productUpdateDTO.getBrand(),product.getCategory(),product.getSeller())) {
            throw new GenericMessageException("Product is already exists!!");
        }

        if(productUpdateDTO.getProductName() != null){
            product.setName(productUpdateDTO.getProductName());
        }
        product.setCancel(productUpdateDTO.getIsCancel());
        product.setReturnable(productUpdateDTO.getIsReturn());
        if(productUpdateDTO.getDescription()!=null){
            product.setDescription(productUpdateDTO.getDescription());
        }
        productRepo.save(product);
    }

    public void updateProductVariation(Long id,ProductVariationDTO productVariationDTO,String metadata) throws IOException {
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

        if(product.getDeleted() || !product.getActive()){
            throw new GenericMessageException("Product is not activated or deleted!!");
        }


        productVariation.setMetadata(metadata);
        productVariation.setQuantityAvailable(productVariationDTO.getQuantity());
        productVariation.setPrice(productVariationDTO.getPrice());
        productVariationRepo.save(productVariation);

        productVariation.setPrimaryImageName(basePath+productVariationDTO.getImage().getOriginalFilename());
        productVariationRepo.save(productVariation);
        productVariationDTO.getImage().transferTo(new File(basePath+productVariationDTO.getImage().getOriginalFilename()));

    }

    public ViewProductDTO viewProductByCustomer(Long productId){
        Product product = productRepo.findById(productId).orElseThrow(()->new ResourcesNotFoundException("No product found for this id!!"));

        if(product.getDeleted() && !product.getActive()){
            throw new GenericMessageException("This product is not available or deleted!!");
        }
        if(Objects.isNull(product.getProductVariation())){
            throw new GenericMessageException("Product variation!!");
        }

        if(!productVariationRepo.existsByProduct(product)){
            throw new GenericMessageException("There is no any product variation exist for this product!!");
        }
        ViewProductDTO viewProductDTO = new ViewProductDTO();
        viewProductDTO.setProductName(product.getName());
        viewProductDTO.setProductId(product.getId());
        viewProductDTO.setBrand(product.getBrand());
        viewProductDTO.setCategoryName(product.getCategory().getName());
        viewProductDTO.setDescription(product.getDescription());
        viewProductDTO.setCategoryId(product.getCategory().getId());

        List<ProductVariation> productVariationList = productVariationRepo.findByProduct(product);
        List<ProductVariationDTO> productVariationDTOList = new ArrayList<>();
        for(ProductVariation productVariation: productVariationList ){
            ProductVariationDTO productVariationDTO = new ProductVariationDTO();
            productVariationDTO.setQuantity(productVariation.getQuantityAvailable());
            productVariationDTO.setPrice(productVariation.getPrice());
            productVariationDTOList.add(productVariationDTO);
        }
        viewProductDTO.setProductVariation(productVariationDTOList);
        return viewProductDTO;

    }

    public List<ProductDTO> viewAllProductByCustomer(Long id){
        Category category = categoryRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("Category Id not found!! "));

//        if(categoryRepo.existsByParentCategory(category)){
//            throw new GenericMessageException("This category is not a leaf node category");
//        }

        List<Product> productList=productRepo.findByCategory(category);
        List<ProductDTO> productDTOList=new ArrayList<>();

        for(Product product: productList) {
            if (!product.getProductVariation().isEmpty()){
                ProductDTO productDTO = new ProductDTO();
                productDTO.setProductName(product.getName());
                productDTO.setBrand(product.getBrand());
                productDTO.setReturnable(product.getReturnable());
                productDTO.setDescription(product.getDescription());
                productDTO.setCategoryId(product.getCategory().getId());
                productDTO.setCancelable(product.getCancel());
                productDTO.setCategoryName(product.getCategory().getName());
                productDTOList.add(productDTO);
            }
        }
        return productDTOList;
    }

    public List<ViewProductDTO> viewSimilarCustomerProducts(Long productId, int size, int offset, String sort, String order) {
        PageRequest pageable = PageRequest.of(offset,size, Sort.Direction.fromString(order),sort);
        Product product1 = productRepo.findById(productId).orElseThrow(()-> new ResourcesNotFoundException("Product not found!!"));
        if(product1.getDeleted()){
            throw new ResourcesNotFoundException("Product is deleted!!");
        }
        Category category = categoryRepo.findById(product1.getCategory().getId()).orElseThrow(()->new ResourcesNotFoundException("Category not found"));
        Page<Product> products = productRepo.findByCategory(category,pageable);
        List<ViewProductDTO> viewProductDtoList = new ArrayList<>();
        for(Product product : products){
            if(!product.getDeleted()){
                List<ProductVariation> productVariations = productVariationRepo.findByProduct(product);
                ViewProductDTO viewProductDto = new ViewProductDTO();
                viewProductDto.setProductId(product.getId());
                viewProductDto.setProductName(product.getName());
                viewProductDto.setBrand(product.getBrand());
                viewProductDto.setDescription(product.getDescription());

                CategoryResponseDTO categoryDto = new CategoryResponseDTO();
                categoryDto.setId(product.getCategory().getId());
                categoryDto.setName(product.getCategory().getName());

                List<ProductVariationDTO> productVariationDtoList = new ArrayList<>();
                for(ProductVariation productVariation : productVariations){
                    ProductVariationDTO productVariationDto = new ProductVariationDTO();
                    productVariationDto.setQuantity(productVariation.getQuantityAvailable());
                    productVariationDto.setPrice(productVariation.getPrice());
                    productVariationDto.setMetadataValues(productVariation.getMetadata());
                    productVariationDtoList.add(productVariationDto);
                }
                viewProductDto.setProductVariation(productVariationDtoList);
                viewProductDtoList.add(viewProductDto);
            }
        }
        return viewProductDtoList;
    }


    public ViewProductDTO viewProductByAdmin(Long id){
        if(!productRepo.existsById(id)){
            throw new GenericMessageException("Id not exists!!");
        }

        Product product = productRepo.findById(id).get();
        ViewProductDTO viewProductDTO = new ViewProductDTO();
        viewProductDTO.setCategoryName(product.getCategory().getName());
        viewProductDTO.setDescription(product.getDescription());
        viewProductDTO.setProductName(product.getName());
        viewProductDTO.setBrand(product.getBrand());
        viewProductDTO.setProductId(product.getId());
        viewProductDTO.setCategoryId(product.getCategory().getId());
//        viewProductDTO.setCategory(product.getCategory());

        List<ProductVariation> productVariationList = productVariationRepo.findByProduct(product);
        List<ProductVariationDTO> productVariationDTOList = new ArrayList<>();
        for(ProductVariation productVariation: productVariationList ){
            ProductVariationDTO productVariationDTO = new ProductVariationDTO();
            productVariationDTO.setQuantity(productVariation.getQuantityAvailable());
            productVariationDTO.setPrice(productVariation.getPrice());
            productVariationDTO.setMetadataValues(productVariation.getMetadata());
            productVariationDTOList.add(productVariationDTO);
        }
        viewProductDTO.setProductVariation(productVariationDTOList);
        return viewProductDTO;
    }

    public List<ViewProductDTO> viewAllProductsByAdmin(int offSet, int size, Sort.Direction orderBy,String sortBy){
        PageRequest page = PageRequest.of(offSet,size, Sort.Direction.ASC,sortBy);
        Page<Product> productsPages = productRepo.findAll(page);
        List<ViewProductDTO> viewProductDTOList=new ArrayList<>();
        for(Product product : productsPages){
            ViewProductDTO viewProductDTO = new ViewProductDTO();
            viewProductDTO.setProductName(product.getName());
            viewProductDTO.setProductId(product.getId());
            viewProductDTO.setBrand(product.getBrand());
            viewProductDTO.setDescription(product.getDescription());
            viewProductDTO.setCategoryId(product.getCategory().getId());
            viewProductDTO.setCategoryName(product.getCategory().getName());

            List<ProductVariation> productVariationList = productVariationRepo.findByProduct(product);
            List<ProductVariationDTO> productVariationListDTO = new ArrayList<>();
            for(ProductVariation productVariation:productVariationList) {
                ProductVariationDTO productVariationDTO = new ProductVariationDTO();
                productVariationDTO.setPrice(productVariation.getPrice());
                productVariationDTO.setQuantity(productVariation.getQuantityAvailable());
                productVariationDTO.setMetadataValues(productVariation.getMetadata());
                productVariationListDTO.add(productVariationDTO);
            }
            viewProductDTO.setProductVariation(productVariationListDTO);
            viewProductDTOList.add(viewProductDTO);
        }
        return viewProductDTOList;



    }

    public boolean activateProduct(Long id){
        Product product=productRepo.findById(id).orElseThrow(()->new ResourcesNotFoundException("No product found for this id!!"));

        String email = product.getSeller().getEmail();
        if(product.getDeleted()){
            throw new GenericMessageException("Product is deleted!!");
        }
        if(!product.getActive()){
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
        if(product.getDeleted()){
            throw new GenericMessageException("Product is deleted!!");
        }
        if(product.getActive()){
            product.setActive(false);
            productRepo.save(product);
            emailService.sendMail(email,"Product Activation Status","Your product has been deactivated now!");
            return true;
        }
        return false;
    }




}

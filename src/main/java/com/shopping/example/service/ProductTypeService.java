package com.shopping.example.service;

import com.shopping.example.entity.Product;
import com.shopping.example.entity.ProductType;
import com.shopping.example.entity.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductTypeService {

    List<ProductType> findByProduct(Product product);

    List<ProductType> getProductTypeByAll();

    ProductType saveProductType(ProductType productType);


    Optional<ProductType> getProductTypeById(Long productTypeId);


    List<ProductType> getBestSellerProductTypes();

    List<ProductType> getProductTypeBySupplier(Long id);


}

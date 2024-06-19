package com.shopping.example.service.impl;

import com.shopping.example.entity.Product;
import com.shopping.example.entity.ProductType;
import com.shopping.example.repository.ProductTypeRepository;
import com.shopping.example.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public List<ProductType> getProductTypeByAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public List<ProductType> findByProduct(Product product) {
        return productTypeRepository.findByProduct(product);
    }

    @Override
    public ProductType saveProductType(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    @Override
    public Optional<ProductType> getProductTypeById(Long productTypeId) {
        return productTypeRepository.findById(productTypeId);
    }
}

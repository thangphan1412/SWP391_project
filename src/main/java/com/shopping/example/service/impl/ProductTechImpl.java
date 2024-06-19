package com.shopping.example.service.impl;

import com.shopping.example.entity.ProductTech;
import com.shopping.example.repository.ProductTechRepository;
import com.shopping.example.service.ProductTechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductTechImpl implements ProductTechService {

    @Autowired
    private ProductTechRepository productTechRepository;

    @Override
    public Optional<ProductTech> getProductTechById(Long id) {
        return productTechRepository.findById(id);
    }

    @Override
    public ProductTech saveTech(ProductTech productTech) {
        return productTechRepository.save(productTech);
    }

    @Override
    public List<ProductTech> getAllProductTechs() {
        return productTechRepository.findAll();
    }

    @Override
    public ProductTech saveProductTech(ProductTech productTech) {
        return productTechRepository.save(productTech);
    }


    @Override
    public List<Integer> getAllProductRam() {
        return productTechRepository.findDistinctRam();
    }

    @Override
    public List<Integer> getAllProductMemory() {
        return productTechRepository.findDistinctMemory();
    }


}

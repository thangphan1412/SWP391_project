package com.shopping.example.service.impl;

import com.shopping.example.entity.Brand;
import com.shopping.example.repository.BrandRepository;
import com.shopping.example.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;


    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }


}

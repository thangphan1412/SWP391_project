package com.shopping.example.service;

import com.shopping.example.entity.Brand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BrandService {
    Optional<Brand> findById(Long id);
    List<Brand> findAll();

    Brand save(Brand brand);

}

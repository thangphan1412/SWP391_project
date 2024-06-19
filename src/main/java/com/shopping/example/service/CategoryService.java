package com.shopping.example.service;

import com.shopping.example.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    Optional<Category> findById(long id);

    List<Category> findAll();

    Category save(Category category);
}

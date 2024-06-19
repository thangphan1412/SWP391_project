package com.shopping.example.service.impl;

import com.shopping.example.entity.Category;
import com.shopping.example.repository.CategoryRepository;
import com.shopping.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryImpl implements CategoryService {
     @Autowired
     private CategoryRepository categoryRepository;



    @Override
    public Optional<Category> findById(long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }


}

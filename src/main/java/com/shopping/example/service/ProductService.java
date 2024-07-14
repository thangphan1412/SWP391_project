package com.shopping.example.service;

import com.shopping.example.entity.Category;
import com.shopping.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<Product> getAllProducts();

    List<Product> getProductsByCategory(Category category);

    Optional<Product> getProductById(long id);

    List<Product> getProductsByName(String productName);

    Product addProduct(Product product);
    Product updateProduct(Product product);


    List<Product> getProductsByPriceRange(double minPrice, double maxPrice);


    Page<Product> getProducts(int page, int size);



     Page<Product> getProductsByCategory(Category category, Pageable pageable);



    Page<Product> getAllProducts(int page, int size);

    Page<Product> filterProducts(String name, Long categoryId, Double minPrice, Double maxPrice,
                                 Long colorId, String memory, String ram, String minSize, String maxSize, Pageable pageable);



}

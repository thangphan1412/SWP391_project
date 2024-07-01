package com.shopping.example.service.impl;

import com.shopping.example.entity.Category;
import com.shopping.example.entity.Product;
import com.shopping.example.entity.Supplier;
import com.shopping.example.repository.ProductRepository;
import com.shopping.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Optional<Product> getProductById(long id){
        return productRepository.findById(id);
    }


    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name.toLowerCase());
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }




    // Phương thức để lấy danh sách tất cả sản phẩm với phân trang
    @Override
    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size); // PageRequest sử dụng page bắt đầu từ 0
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> filterProducts(String name, Long categoryId, Double minPrice, Double maxPrice, Long colorId, String memory, String ram, String minSize, String maxSize, Pageable pageable) {
        return productRepository.filterProducts(name, categoryId, minPrice, maxPrice, colorId, memory, ram, minSize, maxSize, pageable);
    }

    @Override
    public List<Product> findBySuppliers(Supplier supplier) {
        return productRepository.findBySupplier(supplier);
    }


    @Override
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.findByProductTypePriceBetween(minPrice, maxPrice);
    }


    @Override
    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }



    @Override
    public Page<Product> getProductsByCategory(Category category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }


}


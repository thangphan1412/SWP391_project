package com.shopping.example.repository;

import com.shopping.example.entity.Product;
import com.shopping.example.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {


    List<ProductType> findByProduct(Product product);

}

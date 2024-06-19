package com.shopping.example.repository;

import com.shopping.example.entity.ProductTech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductTechRepository extends JpaRepository<ProductTech, Long> {


    @Query("SELECT DISTINCT p.ram FROM ProductTech p")
    List<Integer> findDistinctRam();
    @Query("SELECT DISTINCT p.memory FROM ProductTech p")
    List<Integer> findDistinctMemory();

}

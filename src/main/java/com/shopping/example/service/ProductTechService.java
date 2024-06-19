package com.shopping.example.service;



import com.shopping.example.entity.ProductTech;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductTechService {

    Optional<ProductTech> getProductTechById(Long id);

    ProductTech saveTech(ProductTech productTech);

    List<ProductTech> getAllProductTechs();

    ProductTech saveProductTech(ProductTech productTech);

    List<Integer> getAllProductRam();
    List<Integer> getAllProductMemory();


}

package com.shopping.example.repository;

import com.shopping.example.entity.Category;
import com.shopping.example.entity.Product;
import com.shopping.example.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE %:query%")
    List<Product> findByNameContainingIgnoreCase(@Param("query") String query);

    List<Product> findByCategory(Category category);


    Page<Product> findByCategory(Category category, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.productTypes pt WHERE pt.product_type_price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByProductTypePriceBetween(double minPrice, double maxPrice);



    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN p.productTypes pt " +
            "LEFT JOIN pt.color c " +
            "LEFT JOIN pt.productTech t " +
            "WHERE (:name IS NULL OR p.productName LIKE %:name%) AND " +
            "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
            "(:minPrice IS NULL OR pt.product_type_price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR pt.product_type_price <= :maxPrice) AND " +
            "(:colorId IS NULL OR c.colorId = :colorId) AND " +
            "(:memory IS NULL OR t.memory = :memory) AND " +
            "(:ram IS NULL OR t.ram = :ram) AND " +
            "(:minSize IS NULL OR t.size between :minSize AND :maxSize)")
    Page<Product> filterProducts(@Param("name") String name,
                                 @Param("categoryId") Long categoryId,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice,
                                 @Param("colorId") Long colorId,
                                 @Param("memory") String memory,
                                 @Param("ram") String ram,
                                 @Param("minSize") String minSize,
                                 @Param("maxSize") String maxSize,
                                 Pageable pageable);


    List<Product> findBySupplier(Supplier supplier);


    @Query(value = "SELECT TOP 5 * FROM dbo.product ORDER BY product_id DESC", nativeQuery = true)
    List<Product> findNewArrivalProducts();
}

package com.shopping.example.repository;

import com.shopping.example.entity.Product;
import com.shopping.example.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {


    List<ProductType> findByProduct(Product product);

    @Query(value = "SELECT TOP 5 pt.*, SUM(od.quantity) AS total_quantity_sold " +
            "FROM order_detail od " +
            "JOIN product_typies pt ON od.product_type_id = pt.product_type_id " +
            "GROUP BY pt.product_type_id, pt.product_type_price, pt.product_type_quantity, " +
            "pt.product_type_status, pt.color_id, pt.product_id, pt.tech_id " +
            "ORDER BY total_quantity_sold DESC", nativeQuery = true)
    List<ProductType> productBestSeller();


    @Query("SELECT pt FROM ProductType pt " +
            "JOIN pt.product p " +
            "WHERE p.supplier.id = :supplierId")
    List<ProductType> findAllProductTypesBySupplierId(@Param("supplierId") Long supplierId);


}

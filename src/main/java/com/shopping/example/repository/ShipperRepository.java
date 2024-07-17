package com.shopping.example.repository;

import com.shopping.example.entity.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Long> {
//    @Query("SELECT c FROM  Shipper c WHERE lower(c.name) LIKE %:query% ")
//    List<Shipper> findShipperByName(String lowerCase);
}

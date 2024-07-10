package com.shopping.example.repository;


import com.shopping.example.entity.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface VoucherRepository extends JpaRepository<Voucher, Long> {


    @Query("SELECT v.percentageDiscount FROM Voucher v WHERE v.voucherCode = :voucherCode")
    Double findPercentageDiscountByVoucherCode(@Param("voucherCode") String voucherCode);


}

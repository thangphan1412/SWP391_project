package com.shopping.example.repository;

import com.shopping.example.entity.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {

    List<ReceiptDetail> findByReceiptReceiptId(Long receiptId);

    void deleteByReceiptDetailId(Long receiptDetailId);



}

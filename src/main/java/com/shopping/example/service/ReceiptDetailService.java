package com.shopping.example.service;

import com.shopping.example.entity.ReceiptDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public interface ReceiptDetailService {

    List<ReceiptDetail> findAllbyReceiptId(Long receiptId);


    void deleteByReceiptDetailId(Long receiptDetailId);

    Optional<ReceiptDetail> getReceiptDetail(Long id);

    void saveReceiptDetail(ReceiptDetail receiptDetail);
}
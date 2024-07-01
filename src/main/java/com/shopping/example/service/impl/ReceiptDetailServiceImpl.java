package com.shopping.example.service.impl;

import com.shopping.example.entity.ReceiptDetail;
import com.shopping.example.repository.ReceiptDetailRepository;
import com.shopping.example.service.ReceiptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;


    @Override
    public Optional<ReceiptDetail> getReceiptDetail(Long id) {
        return receiptDetailRepository.findById(id);
    }


}

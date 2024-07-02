package com.shopping.example.service.impl;

import com.shopping.example.entity.ReceiptDetail;
import com.shopping.example.repository.ReceiptDetailRepository;
import com.shopping.example.service.ReceiptDetailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@Transactional
public class ReceiptDetailServiceImpl implements ReceiptDetailService {
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;


    @Override
    public List<ReceiptDetail> findAllbyReceiptId(Long receiptId) {
        return receiptDetailRepository.findByReceiptReceiptId(receiptId);
    }

    @Override
    public void deleteByReceiptDetailId(Long receiptDetailId) {
        receiptDetailRepository.deleteByReceiptDetailId(receiptDetailId);
    }

    @Override
    public Optional<ReceiptDetail> getReceiptDetail(Long id) {
        return receiptDetailRepository.findById(id);
    }






}

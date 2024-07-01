package com.shopping.example.service.impl;

import com.shopping.example.entity.Receipt;
import com.shopping.example.repository.ReceiptRepository;
import com.shopping.example.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ReceiptServiceImpl implements ReceiptService {

    @Autowired
    private ReceiptRepository receiptRepository;


    @Override
    public void addReceipt(Receipt receipt) {
        receiptRepository.save(receipt);
    }

    @Override
    public List<Receipt> getReceipts() {
        return receiptRepository.findAll();
    }





}

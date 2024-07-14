package com.shopping.example.service;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Shipper;

public interface IShipperService {
    Shipper register(RegisterRequest registerRequest);
}

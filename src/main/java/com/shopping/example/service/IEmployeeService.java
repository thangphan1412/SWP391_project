package com.shopping.example.service;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Employee;

public interface IEmployeeService {
    Employee register(RegisterRequest registerRequest);
}

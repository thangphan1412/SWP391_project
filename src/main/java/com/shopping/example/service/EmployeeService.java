package com.shopping.example.service;

import com.shopping.example.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface EmployeeService {

    List<Employee> getAllEmployees();

}

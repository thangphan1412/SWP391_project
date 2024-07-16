package com.shopping.example.service;

import com.shopping.example.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface EmployeeService {

    List<Employee> getAllEmployees();

    void save(Employee employee);

    Optional<Employee> findById(Long id);


}

package com.shopping.example.controller.api;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Account;
import com.shopping.example.entity.Customer;
import com.shopping.example.entity.Employee;
import com.shopping.example.entity.Shipper;
import com.shopping.example.service.CustomerService;
import com.shopping.example.service.impl.EmployeeService;
import com.shopping.example.service.impl.ShipperService;
import com.shopping.example.utility.Contant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ShipperService shipperService;



    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMessages", errorMessages);
            return "redirect:/register";
        }
        try {
            switch (registerRequest.getRole()) {
                case Contant.ROLE_USER:
                    Customer customer = customerService.register(registerRequest);
                    model.addAttribute("customer", customer);
                    break;
                case Contant.ROLE_EMPLOYEE:
                    Employee employee = employeeService.register(registerRequest);
                    model.addAttribute("employee", employee);
                    break;
                case Contant.ROLE_SHIPPER:
                    Shipper shipper = shipperService.register(registerRequest);
                    model.addAttribute("shipper", shipper);
                    break;
                default:
                    break;
            }
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
            return "redirect:/register"; // Name of the success view
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }
//    @PostMapping("register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest,
//                                      BindingResult result) {
//        if (result.hasErrors()) {
//            List<String> errorMessages = result.getFieldErrors()
//                    .stream()
//                    .map(FieldError::getDefaultMessage)
//                    .collect(Collectors.toList());
//            return ResponseEntity.badRequest().body(errorMessages);
//        }
//        try {
//            Customer customer = customerService.register(registerRequest);
////            emailService.sendVerificationCode(customer.getAccount().getEmail(), customer.getAccount().getVerificationCode());
//            return ResponseEntity.ok().body(customer);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("/checkCurrentUserEmail")
    public ResponseEntity<String> checkCurrentUserEmail() {
        Optional<String> userEmail = customerService.getCurrentUserEmail();
        if (userEmail.isPresent()) {
            return ResponseEntity.ok("User email: " + userEmail.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user logged in.");
        }
    }

}

package com.shopping.example.controller.thymleaf;


import com.shopping.example.entity.ProductType;
import com.shopping.example.service.AccountService;
import com.shopping.example.service.CustomerService;
import com.shopping.example.service.ProductService;
import com.shopping.example.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    AccountService accountService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("BestSeller", productTypeService.getBestSellerProductTypes());
        model.addAttribute("ArrivalProd", productService.NewArrivalProduct());
        return "index";
    }

    @GetMapping("/paymentTest")
    public String paymentTest(Model model) {
        return "PaymentTest";
    }







}

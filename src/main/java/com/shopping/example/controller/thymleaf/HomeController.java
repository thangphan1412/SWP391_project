package com.shopping.example.controller.thymleaf;


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
    ProductService productService;

    @Autowired
    ProductTypeService productTypeService;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("BestSeller", productTypeService.getBestSellerProductTypes());
        model.addAttribute("ArrivalProd" ,productService.NewArrival());
        return "index";
    }


    @GetMapping("/lostPassword")
    public String viewLostPassWord(){
        return "lost-password";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(){
        return "reset-password";
    }

    @GetMapping("/changPassword")
    public String changPassword(){
        return "change-passwordCurrent";
    }

}

package com.shopping.example.controller.thymleaf;


import com.shopping.example.service.AccountService;
import com.shopping.example.service.CustomerService;
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

    @GetMapping("/")
    public String home(Model model) {
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

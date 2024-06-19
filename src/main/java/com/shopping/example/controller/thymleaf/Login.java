package com.shopping.example.controller.thymleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Login {
    @GetMapping("/login")
    public String showFormLogin(){
        return "login";

    }
}

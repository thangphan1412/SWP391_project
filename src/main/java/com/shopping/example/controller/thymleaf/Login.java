package com.shopping.example.controller.thymleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Login {
    @GetMapping(value = {"/login", "/logout"})
    public String showFormLogin(){
        return "login";

    }
    @GetMapping("/reset-password")
    public String test(){
        return "login";

    }
}

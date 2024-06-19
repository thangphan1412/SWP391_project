package com.shopping.example.controller.thymleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
public class GlobalExceptionHandle {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        // Log the exception
        e.printStackTrace();
        // Redirect to a custom error page
        return "error"; // You can customize this page as needed
    }


}
